package com.chessgear.data;

import com.chessgear.analysis.EngineResult;
import com.chessgear.game.BoardState;
import com.chessgear.game.Move;
import com.chessgear.server.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameTree builder helper class.
 * Created by Ran on 11/5/2015.
 */
public class GameTreeBuilder {

    private List<BoardState> boardStateList;
    private List<Move> whiteHalfMoves;
    private List<Move> blackHalfMoves;
    private List<GameTreeNode> nodes;
    private int bigid;

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
     */
    public GameTreeBuilder(PGNParser parser) {
        this(parser.getListOfBoardStates(), parser.getWhiteHalfMoves(), parser.getBlackHalfMoves());
    }
    
    public static GameTree constructGameTree(String username){
        /*
         * The purpose of this method is to bake a cute GameTree from the data present in the DataBase.
         * 
         * Don't forget, DatabaseService is a Singleton, thus a call to DatabaseService.getInstance() is sufficient.
         */
        DatabaseService db = DatabaseService.getInstanceOf();
        if (!db.userExists(username))
            return null;
        int rootid = db.getRoot(username);
        Map<GameTreeNode.NodeProperties, String> map = new HashMap<>();
        String board;
        int mult;
        try {
            map = db.fetchNodeProperty(username, rootid);
            board = map.get(GameTreeNode.NodeProperties.BOARDSTATE);
        } catch (IllegalArgumentException e) {
            board = "err";
        }
        try {
            mult = Integer.parseInt(map.get(GameTreeNode.NodeProperties.MULTIPLICITY));
        } catch (NumberFormatException e) {
            System.err.println("Error Fetching");
            mult = 1;
        }
        GameTreeNode root = new GameTreeNode(rootid);
        BoardState boardstate;
        if(!board.equals("err")) {
            boardstate = new BoardState(board);
        } else {
            boardstate = new BoardState();
            boardstate.setToDefaultPosition();
        }
        root.setMultiplicity(mult);
        root.setBoardState(boardstate);
        HashMap<Integer, GameTreeNode> nodemapping = new HashMap<>();
        int NodeCount = makeTree(root, username, nodemapping);
        GameTree tree = new GameTree();
        tree.setNodeMapping(nodemapping);
        tree.setRoot(root);
        tree.setNodeIdCounter(NodeCount);
        return tree;
    }

    /**
     * Recursive call to create a tree from the database;
     * @param base
     * @param email
     * @return
     */
    private static int makeTree(GameTreeNode base, String email, HashMap<Integer, GameTreeNode> nodemap) {
        DatabaseService db = DatabaseService.getInstanceOf();
        List<Integer> children;
        HashMap<Integer, GameTreeNode> nodemapping = new HashMap<>();
        try {
            children = db.childrenFrom(email, base.getId());
        } catch(IllegalArgumentException e) {
            nodemapping.put(base.getId(), base);

            return base.getId();
        }
        int bigid = 0;
        for (int i = 0; i < children.size(); i++) {
            Map<GameTreeNode.NodeProperties, String> map = db.fetchNodeProperty(email, children.get(i));
            String board = map.get(GameTreeNode.NodeProperties.BOARDSTATE);
            String cp = map.get(GameTreeNode.NodeProperties.CP);
            String pv = map.get(GameTreeNode.NodeProperties.PV);
            String bestmove = map.get(GameTreeNode.NodeProperties.BESTMOVE);
            EngineResult engine = new EngineResult();
            engine.setBestMove(bestmove);
            double dcp = Double.parseDouble(cp);
            engine.setCp(dcp);
            engine.setPv(pv);
            int mult;
            try {
                mult = Integer.parseInt(map.get(GameTreeNode.NodeProperties.MULTIPLICITY));
            } catch (NumberFormatException e) {
                System.err.println("Error Fetching");
                mult = 1;
            }
            GameTreeNode next = new GameTreeNode(children.get(i));
            BoardState boarstate = new BoardState(board);
            next.setMultiplicity(mult);
            next.setBoardState(boarstate);
            next.setEngineResult(engine);
            /*
            Need to add Engine result here, not sure how it is stored.
             */
            base.addChild(next);
            next.setParent(base);
            int id = makeTree(next, email, nodemap);
            if (bigid < id)
                bigid = id;
        }
        nodemapping.put(base.getId(), base);
        if(bigid > base.getId()) {
            return bigid;
        } else {
            return bigid;
        }
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
}
