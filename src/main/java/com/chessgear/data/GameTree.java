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
     * Counter for node ints.
     */
    private int nodeIntCounter;

    /**
     * Adds a game to the tree.
     * @param gameTreeNodes
     */
    public void addGame(List<GameTreeNode> gameTreeNodes) {
        // TODO
    }

    public GameTree(GameTreeNode root){
        this.root = root;
    }

    /**
     * Accessor function to get the root of the GameTree
     * @return the root of the GameTree
     */
    public GameTreeNode getRoot(){
        return this.root;
    }
}
