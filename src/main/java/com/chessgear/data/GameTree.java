package com.chessgear.data;

import com.chessgear.analysis.Engine;
import com.chessgear.analysis.EngineResult;
import com.chessgear.analysis.OsUtils;
import com.chessgear.data.GameTreeNode.NodeProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.chessgear.data.GameTreeNode.NodeProperties.*;

/**
 * Game Tree data structure.
 * Created by Ran on 10/8/2015.
 */
public class GameTree {

    public static final char END_OF_CHILDREN = '$';

    private static final DatabaseService db = DatabaseService.getInstanceOf();

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

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(GameTree.class);

    /**
     * Initializes a new empty game tree.
     */
    public GameTree() {
        this.nodeIdCounter = 0;
        this.nodeMapping = new HashMap<>();
        this.root = GameTreeNode.rootNode(nodeIdCounter);
        this.nodeMapping.put(nodeIdCounter++, root);
    }

    private void placeIn(GameTreeNode currentGameNode, List<GameTreeNode> newNodes, String username){
        if(newNodes.size() == 0)
            return;

        GameTreeNode toPlace = newNodes.remove(0);
        boolean childFound = false;

        //first we see if any child node is equivalent to this node.
        for(GameTreeNode child : currentGameNode.getChildren()){
            if(child.getBoardState().equals(toPlace)){
                //we have found a common ancestor!
                child.incrementMultiplicity();
                db.updateNodeProperty(username, child.getId(), MULTIPLICITY, "" + child.getMultiplicity());
                placeIn(child, newNodes, username); //the recursive call
                childFound = true;

                break; //we exit out of the loop, because no other child can have the same FEN.
            }
        }

        //if no child was found, we have to follow our own path (= make our own child ;-) )
        if(!childFound){
            Engine engine = new Engine((new OsUtils()).getBinaryLocation());

            GameTreeNode parent = currentGameNode;
            for(GameTreeNode child: newNodes){
                //we establish relationship
                child.setParent(parent);
                parent.addChild(parent);

                //now we set the properties of our child
                child.setMultiplicity(1);
                child.setId(nodeIdCounter);
                EngineResult engineResult = null;
                try {
                    engineResult = engine.analyseFEN(child.getBoardState().toFEN(), 100);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                child.setEngineResult(engineResult);

                //we put the node in nodeMapping
                nodeMapping.put(nodeIdCounter, child);

                //and update the count
                nodeIdCounter++;

                //Second step: save the node in the actual database
                HashMap<NodeProperties, String> properties = new HashMap<>();
                properties.put(BOARDSTATE, child.getBoardState().toFEN());
                properties.put(MULTIPLICITY, "" + child.getMultiplicity());
                properties.put(BESTMOVE, engineResult.getBestMove());
                properties.put(CP, ""+engineResult.getCp());
                properties.put(PV, engineResult.getPv());
                db.addNode(username, child.getId(), properties);
                db.addChild(username, parent.getId(), child.getId());

                parent = child; //cycle of life   
            }
            try {
                engine.terminateEngine();
            } catch (Exception e) {
                logger.error(e.getMessage());;
            }
        }

    }

    /**
     * Place the list of nodes in the current GameTree at the right place.
     * 
     * @param newNodes a list of game tree node
     * @param username the username of User that will add game to
     * @throws Exception if error occurs while adding a game to gameTree
     */
    public void addGame(List<GameTreeNode> newNodes, String username) throws Exception {
        /*
         * Gilbert's comment:
         * This method is at the heart of the program. It has to update both the current GameTree and the Database.
         * 
         * Maybe we should find a way to place those two tasks in two different methods. But I choose not to do so
         * because it would make a lot of overhead work. It is definitely a tradeoff.
         * 
         * NOTE: The algorithm that follows makes the assumption that the newNodes arive in the right order.
         *       I did so because it was the initial idea in Neil's code. Also, it assume that newNodes comes
         *       with NO initial FEN node. The criteria of equivalence between node is also the FEN which is somewaht 
         *       weird.
         */

        //first we update the root. Since it's the initial situation (before any move), it is in every chess play.
        root.incrementMultiplicity();
        db.updateNodeProperty(username,  root.getId(), MULTIPLICITY, "" + root.getMultiplicity());
        
        int before = nodeIdCounter; //just for logg and debug, can remove that after.
        
        //now just let the magic of recursivity work
        placeIn(root, newNodes, username);
        
        int after = nodeIdCounter; ////just for logg and debug, can remove that after.
        
        logger.info("Gilbert's code says: I added a game to user " + username +
                ". Before, the biggest id in the tree was" + before + ", now it is " + after + ".");
        
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

    public void setNodeIdCounter(int high) {
        nodeIdCounter = high;
    }

}
