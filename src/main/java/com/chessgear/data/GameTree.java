package com.chessgear.data;

import com.chessgear.analysis.Engine;
import com.chessgear.analysis.EngineResult;
import com.chessgear.analysis.OsUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Object representation of the Game Tree
 */
public final class GameTree {

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
     * @param username the username of User that will add game to
     * @throws IOException if error occurs while adding a game to gameTree
     */
    public void addGame(List<GameTreeNode> gameTreeNodes, String username) throws IOException {
        OsUtils osUtils = new OsUtils();
        Engine engine = new Engine(osUtils.getBinaryLocation());
        this.root.incrementMultiplicity();
        DatabaseWrapper.getInstance().setMultiplicity(username, this.root.getId(), this.root.getMultiplicity());

        GameTreeNode currentNode = this.root;
        for (int c = 1; c < gameTreeNodes.size(); c++) {
            GameTreeNode candidateChildNode = gameTreeNodes.get(c);
            boolean childFound = false;
            List<GameTreeNode> currentChildren = currentNode.getChildren();
            for (GameTreeNode n : currentChildren) {
                if (n.getBoardState().equals(candidateChildNode.getBoardState())) {
                    n.incrementMultiplicity();
                    DatabaseWrapper.getInstance().setMultiplicity(username, n.getId(), n.getMultiplicity());
                    currentNode = n;
                    childFound = true;
                    break;
                }
            }

            // If we didn't find it, then we make a new node.
            if (!childFound) {
                candidateChildNode.setMultiplicity(1);
                candidateChildNode.setId(this.nodeIdCounter);
                EngineResult engineResult = engine.analyseFEN(candidateChildNode.getBoardState().toFEN(), 100);
                candidateChildNode.setEngineResult(engineResult);
                this.nodeMapping.put(this.nodeIdCounter++, candidateChildNode);
                currentNode.addChild(candidateChildNode);
                DatabaseWrapper.getInstance().addChild(username, currentNode, candidateChildNode);
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
     * @param root GameTreeNode root to be set to
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

    /**
     * Mutator for the value of the node counter.
     * @param high New value for the node counter.
     */
    public void setNodeIdCounter(int high) {
        nodeIdCounter = high;
    }

    /**
     * Accessor for the nodeMapping.
     * @return nodeMapping for this tree.
     */
    public HashMap<Integer, GameTreeNode> getNodeMapping() {
        return this.nodeMapping;
    }

}
