package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.game.Move;

import java.util.ArrayList;
import java.util.List;

import static com.chessgear.data.GameTree.END_OF_CHILDREN;

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
    private final List<GameTreeNode> children;

    /**
     * Id of this node.
     */
    private final int id;

    /**
     * Engine eval of the position.
     */
    private double eval;

    /**
     * Engine continuation.
     */
    private String engineLine;

    /**
     * The last move that was made before this position was achieved.
     */
    private Move lastMoveMade;
    
    
    
    //TODO: create a real constructor
    public GameTreeNode(int id){
        this.previous = null;
        this.children = new ArrayList<>();
        this.id = id;
    }
    
    public void addChild(GameTreeNode child){
        children.add(child);
        child.setParent(this);
    }
    
    public void setParent(GameTreeNode parent){
        this.previous = parent;
    }
    
    public boolean isRoot(){
        return previous == null;
    }
    
    public boolean isLeaf(){
        return children.size() == 0;
    }
    
    public enum NodeProperties{
        EVAL
    }
    

}
