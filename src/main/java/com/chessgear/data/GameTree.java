package com.chessgear.data;

import com.chessgear.analysis.Engine;
import com.chessgear.analysis.EngineResult;
import com.chessgear.analysis.OsUtils;

import javax.xml.crypto.Data;
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
     * @throws Exception if error occurs while adding a game to gameTree
     */
    public void addGame(List<GameTreeNode> gameTreeNodes, String username) throws Exception {
        DatabaseService db = DatabaseService.getInstanceOf();
        OsUtils osUtils = new OsUtils();
        Engine engine = new Engine(osUtils.getBinaryLocation());
        this.root.incrementMultiplicity();
        Integer rootmult = root.getMultiplicity();
        try {
            db.updateNodeProperty(username, this.root.getId(), GameTreeNode.NodeProperties.MULTIPLICITY, rootmult.toString());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
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
                    Integer mult = n.getMultiplicity();
                    String multi = mult.toString();
                    try {
                        db.updateNodeProperty(username, n.getId(), GameTreeNode.NodeProperties.MULTIPLICITY, multi);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                    //.updateNodeProperty();
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
                HashMap<GameTreeNode.NodeProperties, String> props = new HashMap<>();
                props.put(GameTreeNode.NodeProperties.BOARDSTATE,candidateChildNode.getBoardState().toFEN());
                Integer mult = candidateChildNode.getMultiplicity();
                props.put(GameTreeNode.NodeProperties.MULTIPLICITY, mult.toString());
                props.put(GameTreeNode.NodeProperties.BESTMOVE, engineResult.getBestMove());
                Double cp = engineResult.getCp();
                props.put(GameTreeNode.NodeProperties.CP, cp.toString());
                props.put(GameTreeNode.NodeProperties.PV, engineResult.getPv());
                try {
                    db.addNode(username, candidateChildNode.getId(), props);
                } catch (IllegalArgumentException e) {
                    if(!e.getMessage().equals("Node already exists in database"))
                        System.err.println(e.getMessage());
                }
                try {
                    //bSystem.out.println(currentNode.getId() + " and " + candidateChildNode.getId());
                    db.addChild(username, currentNode.getId(), candidateChildNode.getId());
                } catch (IllegalArgumentException e) {
                    if(e.getMessage() != "this child already has a parent!")
                        System.err.println(e.getMessage());
                }
                currentNode = candidateChildNode;
                //also update in the database

            }

        }
        engine.terminateEngine();
        
        //TODO: make the update in the database.
        /*
         * The update made to the tree should be repercuted in the database.
         * 
         */


    }

    /**
     * Accessor function to get the root of the GameTree
     * @return the root of the GameTree
     */
    public GameTreeNode getRoot(){
        return this.root;
    }

    /**
     * Setting the root
     */
    public void setRoot(GameTreeNode roo) {
        root = roo;
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
