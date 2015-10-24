package com.chessgear.data;

import com.chessgear.game.Game;

import java.util.HashMap;

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
     * Adds game to tree.
     * @param g Game to add to tree.
     */
    public void addGame(Game g) {
        // TODO
    }

    public GameTree(GameTreeNode root){
        this.root = root;
    }
    }
