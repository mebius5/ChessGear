package com.chessgear.data;

import com.chessgear.game.Game;

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
        // TODO
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
     * @param id
     * @return
     */
    public GameTreeNode getNodeWithId(int id) {
        return nodeMapping.get(id);
    }
}
