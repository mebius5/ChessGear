package com.chessgear.data;

import com.chessgear.analysis.Engine;
import com.chessgear.analysis.EngineResult;
import com.chessgear.analysis.OsUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Game Tree data structure.
 * Created by Ran on 10/8/2015.
 */
public class GameTree {

    public static final char END_OF_CHILDREN = '$';
    
    /**
     * Root node of the game tree.
     */
    private GameTreeNode root;

    /**
     * Hash mapping from node id to tree nodes.
     */
    private HashMap<Integer, GameTreeNode> nodeMapping;

    /**
     * Counter for node ids.
     */
    private int nodeIdCounter;

    /**
     * Initializes a new empty game tree.
     */
    public GameTree() {
        this.nodeIdCounter = 0;
        this.nodeMapping = new HashMap<>();
        this.root = GameTreeNode.rootNode(nodeIdCounter);
        this.nodeMapping.put(nodeIdCounter++, root);
    }

    /**
     * Adds a game to the tree.
     * @param gameTreeNodes a list of game tree node
     * @param username name of User where the game will be added to
     * @throws Exception if error occurs while adding a game to gameTree
     */
    public void addGame(List<GameTreeNode> gameTreeNodes) throws Exception {
        OsUtils osUtils = new OsUtils();
        Engine engine = new Engine(osUtils.getBinaryLocation());
        if (this.root == null) {
            this.root = gameTreeNodes.get(0);
            this.root.setMultiplicity(1);
            this.root.setId(this.nodeIdCounter);
            this.nodeMapping.put(this.nodeIdCounter++, this.root); // Add to hashmapping.
        }

        GameTreeNode currentNode = this.root;
        for (int c = 1; c < gameTreeNodes.size(); c++) {
            GameTreeNode candidateChildNode = gameTreeNodes.get(c);

            boolean childFound = false;
            List<GameTreeNode> currentChildren = currentNode.getChildren();
            for (GameTreeNode n : currentChildren) {
                if (n.getBoardState().equals(candidateChildNode.getBoardState())) {
                    n.incrementMultiplicity();
                    currentNode = n;
                    childFound = true;
                    break;
                }
            }

            // If we didn't find it, then we make a new node.
            if (!childFound) {
                candidateChildNode.setMultiplicity(1);
                candidateChildNode.setId(this.nodeIdCounter);
                //System.out.println(candidateChildNode.getBoardState().toFEN());
                EngineResult engineResult = engine.analyseFEN(candidateChildNode.getBoardState().toFEN(), 100);
                candidateChildNode.setEngineResult(engineResult);
                this.nodeMapping.put(this.nodeIdCounter++, candidateChildNode);
                currentNode.addChild(candidateChildNode);
                currentNode = candidateChildNode;
            }

        }
        engine.terminateEngine();
    }

    /**
     * Accessor function to get the root of the GameTree
     * @return the root of the GameTree
     */
    public GameTreeNode getRoot(){
        return this.root;
    }

    /***
     * Setting the root of the gameTree
     * @param root New root to be set to
     */
    public void setRoot(GameTreeNode root) {
        this.root = root;
    }

    public void setNodeMapping(HashMap<Integer, GameTreeNode> e) {
        nodeMapping = e;
    }
    /**
     * Gets the node with the specified id.
     * @param id Id to get node for.
     * @return game tree node with the Id requrested
     */
    public GameTreeNode getNodeWithId(int id) {
        return nodeMapping.get(id);
    }

    /**
     * Determines whether or not the tree contains a node with a particular id.
     * @param id Id of node to check for.
     * @return True if contained in tree, else false.
     */
    public boolean containsNode(int id) {
        return nodeMapping.containsKey(id);
    }

    public void setNodeIdCounter(int high) {
        nodeIdCounter = high;
    }

}
