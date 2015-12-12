package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.server.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neil on 12/7/2015.
 * DatabaseWrapper class that incorporates database with other classes
 */
public class DatabaseWrapper {
    /**
     * The Database we are accessing
     */
    DatabaseService db;
    /**
     * the biggest ID
     */
    int bigid;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(DatabaseWrapper.class);

    /**
     * The database wrapper
     * @param database the database
     */
    public DatabaseWrapper(DatabaseService database) {
        db = database;
    }
    /**
     * @param username the username
     * @return the user object
     */
    public User getUser(String username) {
        if (!db.userExists(username))
            return null;
        int rootid = db.getRoot(username);
        Map<User.Property, String> maps = db.fetchUserProperties(username);
        String corr = maps.get(User.Property.PASSWORD);
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
            logger.error("Error Fetching");
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
        bigid = 0;
        HashMap<Integer, GameTreeNode> nodemapping = makeTree(root, username);
        GameTree tree = new GameTree();
        tree.setNodeMapping(nodemapping);
        tree.setRoot(root);
        tree.setNodeIdCounter(bigid);
        User user = new User(username, corr);
        user.setGameTree(tree);
        return user;
    }

    public HashMap<Integer, GameTreeNode> makeTree(GameTreeNode base, String email) {
        List<Integer> children;
        HashMap<Integer, GameTreeNode> nodemapping = new HashMap<>();
        logger.info("hey");
        try {
            children = db.childrenFrom(email, base.getId());
        } catch(IllegalArgumentException e) {
            nodemapping.put(base.getId(), base);
            if(base.getId() > bigid) {
                bigid = base.getId();
            }
            return nodemapping;
        }

        for (Integer aChildren : children) {
            Map<GameTreeNode.NodeProperties, String> map = db.fetchNodeProperty(email, aChildren);
            String board = map.get(GameTreeNode.NodeProperties.BOARDSTATE);
            int mult;
            try {
                mult = Integer.parseInt(map.get(GameTreeNode.NodeProperties.MULTIPLICITY));
            } catch (NumberFormatException e) {
                logger.error("Error Fetching");
                mult = 1;
            }
            GameTreeNode next = new GameTreeNode(aChildren);
            BoardState boarstate = new BoardState(board);
            next.setMultiplicity(mult);
            next.setBoardState(boarstate);
            base.addChild(next);
            next.setParent(base);
            nodemapping = makeTree(next, email);
        }
        nodemapping.put(base.getId(), base);
        if(base.getId() > bigid) {
            bigid = base.getId();
        }
        return nodemapping;
    }
    public int updateUser(User user) {
        String username = user.getUsername();
        int rootid = db.getRoot(username);
        GameTree tree = user.getGameTree();
        deleteTree(username, rootid);
        storeTree(username, tree, rootid);
        return 1;
    }

    public int storeTree(String email, GameTree gametree, int id) {
        GameTreeNode curr = gametree.getNodeWithId(id);
        List<GameTreeNode> children = curr.getChildren();
        try {
            for (GameTreeNode temp : children) {
                storeTree(email, gametree, temp.getId());
            }
        } catch (NullPointerException e) {
            GameTreeNode temp;
        }
        HashMap<GameTreeNode.NodeProperties, String> pmap = new HashMap<>();
        pmap.put(GameTreeNode.NodeProperties.BOARDSTATE, curr.getBoardState().toFEN());
        pmap.put(GameTreeNode.NodeProperties.MULTIPLICITY, String.valueOf(curr.getMultiplicity()));
        db.addNode(email, id, pmap);
        return 1;
    }
    /**
     * For deleting the tree
     * @param username username of User that will have tree deleted from
     * @param id id of the tree
     * @return 1 if delete successful and 0 if unsuccessful
     */
    public int deleteTree (String username, int id) {
        List<Integer> children;
        try{
            children = db.childrenFrom(username, id);
        } catch (IllegalArgumentException e) {
            try {
                db.deleteNode(username, id);
            } catch (IllegalArgumentException b) {
                return 0;
            }
            return 0;
        }
        for (Integer aChildren : children) {
            deleteTree(username, aChildren);
        }
        db.deleteNode(username, id);
        return 1;
    }
}
