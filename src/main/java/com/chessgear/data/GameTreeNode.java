package com.chessgear.data;

import com.chessgear.analysis.EngineResult;
import com.chessgear.game.BoardState;
import com.chessgear.game.Move;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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

    /***
     * Engine result of the node returned by the stockfish engine
     */
    private EngineResult engineResult;

    /**
     * Counts the number of times this node has occurred.
     */
    private int multiplicity;

    /**
     * The last move that was made before this position was achieved.
     */
    private Move lastMoveMade;

    /**
     * Constructs a node with integer id.
     * @param id Id of this node.
     */
    public GameTreeNode(int id){
        this.previous = null;
        this.children = new ArrayList<>();
        this.id = id;
        this.multiplicity = 1;
    }

    public static GameTreeNode rootNode(int id) {
        GameTreeNode result = new GameTreeNode(id);
        result.multiplicity = 0;
        result.boardState = new BoardState();
        result.boardState.setToDefaultPosition();
        return result;
    }

    /**
     * Accessor for the children of this node.
     * @return children of this node
     */
    public List<GameTreeNode> getChildren() {
        return this.children;
    }

    /**
     * Accessor for multiplicity.
     * @return Multiplicity of this node.
     */
    public int getMultiplicity() {
        return this.multiplicity;
    }

    /**
     * Mutator for multiplicity.
     * @param multiplicity Multiplicity of this node.
     */
    public void setMultiplicity(int multiplicity) {
        this.multiplicity = multiplicity;
    }

    /**
     * Increments multiplicity.
     */
    public void incrementMultiplicity() {
        this.multiplicity++;
    }

    /**
     * Mutator for id.
     * @param id New id for node.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Accessor for id.
     * @return Id of node.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Adds a child node.
     * @param child Child node.
     */
    public void addChild(GameTreeNode child){
        children.add(child);
        child.setParent(this);
    }

    /**
     * Accessor for parent of this node.
     * @return Parent of the current node.
     */
    public GameTreeNode getParent() {
        return this.previous;
    }

    /**
     * Mutator for parent reference.
     * @param parent Parent node of this node.
     */
    public void setParent(GameTreeNode parent){
        this.previous = parent;
    }

    /**
     * Mutator for boardstate.
     * @param b New boardstate for this node.
     */
    public void setBoardState(BoardState b) {
        this.boardState = b;
    }

    /**
     * Accessor for boardstate.
     * @return Boardstate.
     */
    public BoardState getBoardState() {
        return this.boardState;
    }

    /**
     * Mutator for last move made.
     * @param m Last move made for this node.
     */
    public void setLastMoveMade(Move m) {
        this.lastMoveMade = m;
    }

    /**
     * Accessor for the last move made.
     * @return Last move made before this position.
     */
    public Move getLastMoveMade() {
        return this.lastMoveMade;
    }

    /***
     * Mutator for the engine result of the node
     * @param engineResult the engine result of the boardstate of the node by the Stockfish engine
     */
    public void setEngineResult(EngineResult engineResult) {
        this.engineResult = engineResult;
    }

    /***
     * Accessor for the engine result of the node
     * @return the engineResult of the node
     */
    public EngineResult getEngineResult(){
        return this.engineResult;
    }

    /**
     * Checks if this is the root node.
     * @return True if root node, else false.
     */
    public boolean isRoot(){
        return previous == null;
    }

    /**
     * Checks if this is a leaf node.
     * @return True if this is a leaf node, else false.
     */
    public boolean isLeaf(){
        return children.size() == 0;
    }

    /**
     * Accessor for JSON string representation of children.
     * @return
     */
    public String getChildrenJson() {
        List<ChildNodeJson> jsonChildren = new ArrayList<>();
        for (GameTreeNode n : this.children) {
            jsonChildren.add(new ChildNodeJson(n));
        }
        return new GsonBuilder().serializeNulls().create().toJson(jsonChildren);
    }

    /**
     * Gets the JSON for this node.
     * @return
     */
    public String getJson() {
        return new GsonBuilder().serializeNulls().create().toJson(new GameTreeNodeJson(this));
    }

    /**
     * Helper class for converting this class to JSON.
     */
    private static class GameTreeNodeJson {
        private String boardState;
        private List<ChildNodeJson> children;
        private Integer previousNodeId;

        GameTreeNodeJson(GameTreeNode node) {
            this.boardState = node.boardState.toFEN();
            this.children = new ArrayList<>();
            for (GameTreeNode n : node.getChildren()) {
                this.children.add(new ChildNodeJson(n));
            }
            if (node.getParent() != null) {
                this.previousNodeId = node.getParent().getId();
            } else {
                this.previousNodeId = null;
            }
        }
    }

    /**
     * Container class for conversion of children to JSON.
     */
    private static class ChildNodeJson {

        private int id;
        private String name;

        ChildNodeJson(GameTreeNode node) {
            this.id = node.getId();
            if (node.getLastMoveMade() != null) {
                this.name = node.getLastMoveMade().toString();
            } else {
                this.name = null;
            }

        }

    }

    
    public enum NodeProperties{
        EVAL, BOARDSTATE, MULTIPLICITY
    }
    

}
