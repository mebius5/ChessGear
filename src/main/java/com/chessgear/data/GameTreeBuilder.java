package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.game.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * GameTree builder helper class.
 * Created by Ran on 11/5/2015.
 */
public class GameTreeBuilder {

    private List<BoardState> boardStateList;
    private List<Move> whiteHalfMoves;
    private List<Move> blackHalfMoves;
    private List<GameTreeNode> nodes;
    
    //A quick reference
    private static final DatabaseService db = DatabaseService.getInstanceOf();

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(GameTreeBuilder.class);

    /**
     * Constructor. We pass the list of boardstates encountered over the course of a game, a list of white's half moves, and a list of black's half moves.
     * @param boardStateList List of board states.
     * @param whiteHalfMoves List of white's half moves.
     * @param blackHalfMoves List of black's half moves.
     */
    public GameTreeBuilder(List<BoardState> boardStateList, List<Move> whiteHalfMoves, List<Move> blackHalfMoves) {
        this.boardStateList = boardStateList;
        this.whiteHalfMoves = whiteHalfMoves;
        this.blackHalfMoves = blackHalfMoves;
    }

    /**
     * Constructor with PGN parser as input.
     * @param parser PGN Parser.
     * @throws Exception if error occurs during building of game tree
     */
    public GameTreeBuilder(PGNParser parser) throws Exception{
        this(parser.getListOfBoardStates(), parser.getWhiteHalfMoves(), parser.getBlackHalfMoves());
    }

    /**
     * Output of our class. Returns a list of nodes that represents the game.
     * @return List of GameTreeNodes containing the boardstates of the game and the last moves made.
     */
    public List<GameTreeNode> getListOfNodes() {
        if (this.nodes != null) return this.nodes;

        this.nodes = new ArrayList<>();
        int counter = 0;
        for (BoardState b : this.boardStateList) {
            // Let the data structure assign ids. For now, set to 0.
            GameTreeNode currentNode = new GameTreeNode(0);
            currentNode.setBoardState(b);
            if (counter != 0) {
                int index = (counter - 1) / 2;
                // White's last move was this.
                if (counter % 2 != 0) {
                    currentNode.setLastMoveMade(this.whiteHalfMoves.get(index));
                } else {
                    currentNode.setLastMoveMade(this.blackHalfMoves.get(index));
                }
            }
            this.nodes.add(currentNode);
            counter++;
        }

        return this.nodes;
    }
    
    /**
     * Rebuild the GameTree of an user from what was previously stored in the database.
     * 
     * @param username The name of the user to whom belongs the GameTree.
     * 
     * @return A handy GameTree with no worries for the persistence layer.
     */
    public static GameTree fetchGameTreeFromDatabase(String username){
        if (!db.userExists(username))
            return null;
                
        //First, we have to reconstruct the root of the gameTree
        int rootid = db.getRoot(username);
        GameTreeNode root = bakeRootNode(username, rootid);
        
        //Second, we need the node mapping
        HashMap<Integer, GameTreeNode> nodemapping = new HashMap<>();
        nodemapping.put(root.getId(), root);
        int biggestID = addChildren(root, username, nodemapping);//this is where the recursion happens

        //Third, we construct the actual tree
        GameTree toReturn = new GameTree();
        toReturn.setNodeMapping(nodemapping);
        toReturn.setRoot(root);
        toReturn.setNodeIdCounter(biggestID);
        
        logger.info("Gilbert's code says: Someone asked to fetch the tree of " + username + " back from the database.");
        logger.info("Gilbert's code says: also, for now, the biggest id is " + biggestID + ".");
        return toReturn;
    }
    
    private static int addChildren(GameTreeNode parent, String username, HashMap<Integer, GameTreeNode> nodemap) {
        //take back all children
        List<Integer> children = db.childrenFrom(username, parent.getId());

        //put parent in the map
        nodemap.put(parent.getId(), parent);
        
        int globalBiggestId = parent.getId();
        
        //put children in the map
        for(int childrenId : children){            
            GameTreeNode curr = bakeRegularNode(username, childrenId, parent);
            int biggestId = addChildren(curr, username, nodemap);
            
            //update the bigest id found
            globalBiggestId = Math.max(globalBiggestId, biggestId);
        }

        return globalBiggestId;
    }

    private static GameTreeNode bakeRootNode(String username, int nodeId){
        GameTreeNode toReturn = new GameTreeNode(nodeId);
        
        //fetching back info of the node
        Map<GameTreeNode.NodeProperties, String> properties = db.fetchNodeProperty(username, nodeId);

        int mult = Integer.parseInt(properties.get(GameTreeNode.NodeProperties.MULTIPLICITY));
        toReturn.setMultiplicity(mult);
        
        BoardState boardState = new BoardState(properties.get(GameTreeNode.NodeProperties.BOARDSTATE));
        toReturn.setBoardState(boardState);
        
        return toReturn;

    }
    
    private static GameTreeNode bakeRegularNode(String username, int nodeId, GameTreeNode parent){
        GameTreeNode toReturn = new GameTreeNode(nodeId);
        
        //fetching back info of the node
        Map<GameTreeNode.NodeProperties, String> properties = db.fetchNodeProperty(username, nodeId);

        int mult = Integer.parseInt(properties.get(GameTreeNode.NodeProperties.MULTIPLICITY));
        toReturn.setMultiplicity(mult);
        
        BoardState boardState = new BoardState(properties.get(GameTreeNode.NodeProperties.BOARDSTATE));
        toReturn.setBoardState(boardState);
        
        //now the engine results
        EngineResult engine = new EngineResult();
        
        String bestmove = properties.get(GameTreeNode.NodeProperties.BESTMOVE);
        engine.setBestMove(bestmove);
        
        double cp = new Double(properties.get(GameTreeNode.NodeProperties.CP));
        engine.setCp(cp);
        
        String pv = properties.get(GameTreeNode.NodeProperties.PV);
        engine.setPv(pv);

        toReturn.setEngineResult(engine);
        
        parent.addChild(toReturn);
        toReturn.setParent(parent);
        
        return toReturn;
    }
    
}
