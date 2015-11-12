package com.chessgear.data;

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
        this.root = null;
    }

    /**
     * Adds a game to the tree.
     * @param gameTreeNodes
     */
    public void addGame(List<GameTreeNode> gameTreeNodes) {

        if (this.root == null) {
            this.root = gameTreeNodes.get(0);
            this.root.setMultiplicity(1);
            this.root.setId(this.nodeIdCounter++);
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
                candidateChildNode.setId(this.nodeIdCounter++);
                currentNode.addChild(candidateChildNode);
                currentNode = candidateChildNode;
            }

        }

    }

    /**
     * Accessor function to get the root of the GameTree
     * @return the root of the GameTree
     */
    public GameTreeNode getRoot(){
        return this.root;
    }

    /**
     * Gets the node with the specified id.
     * @param id Id to get node for.
     * @return
     */
    public GameTreeNode getNodeWithId(int id) {
        return nodeMapping.get(id);
    }

}
