package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.game.Move;

import java.util.List;

/**
 * Node class for GameTree.
 * Created by Ran on 10/8/2015.
 */
public class GameTreeNode {

    /**
     * Pointer to parent node.
     */
    private GameTreeNode previous;

    /**
     * Boardstate of this node.
     */
    private BoardState boardState;

    /**
     * List of child nodes.
     */
    private List<GameTreeNode> children;

    /**
     * Id of this node.
     */
    private int id;

    /**
     * Engine eval of the position.
     */
    private double eval;

    /**
     * Engine continuation.
     */
    private String engineLine;
}
