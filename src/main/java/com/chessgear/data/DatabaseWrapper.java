package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.game.Game;
import com.chessgear.server.User;
import com.chessgear.server.UserNoDb;
import com.chessgear.data.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neil on 12/7/2015.
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
    public UserNoDb getUser(String username) {
        if (!db.userExists(username))
            return null;
        int rootid = db.getRoot(username);
        Map<UserNoDb.Property, String> maps = db.fetchUserProperties(username);
        String corr = maps.get(UserNoDb.Property.PASSWORD);
        Map<GameTreeNode.NodeProperties, String> map = new HashMap<>();
        String board;
        try {
            map = db.fetchNodeProperty(username, rootid);
            board = map.get(GameTreeNode.NodeProperties.BOARDSTATE);
        } catch (IllegalArgumentException e) {
            board = "err";
        }
        try {
            Integer.parseInt(map.get(GameTreeNode.NodeProperties.MULTIPLICITY));
        } catch (NumberFormatException e) {
            System.err.println("Error Fetching");
        }
        GameTreeNode root = new GameTreeNode(rootid);
        BoardState boardstate;
        if(!board.equals("err")) {
            boardstate = new BoardState(board);
        } else {
            boardstate = new BoardState();
            boardstate.setToDefaultPosition();
        }
        root.setBoardState(boardstate);
        bigid = 0;
        HashMap<Integer, GameTreeNode> nodemapping = makeTree(root, username);
        GameTree tree = new GameTree();
        tree.setNodeMapping(nodemapping);
        tree.setRoot(root);
        tree.setNodeIdCounter(bigid);
        UserNoDb user = new UserNoDb(username, corr);
        user.setGameTree(tree);
        return user;
    }
    public int updateUser(UserNoDb user) {
        return 1;
    }
    public HashMap<Integer, GameTreeNode> makeTree(GameTreeNode base, String email) {
        List<Integer> children;
        HashMap<Integer, GameTreeNode> nodemapping = new HashMap<>();
        System.out.println("hey");
        try {
            children = db.childrenFrom(email, base.getId());
        } catch(IllegalArgumentException e) {
            nodemapping.put(base.getId(), base);
            if(base.getId() > bigid) {
                bigid = base.getId();
            }
            return nodemapping;
        }
        for (int i = 0; i < children.size(); i++) {
            Map<GameTreeNode.NodeProperties, String> map = db.fetchNodeProperty(email, children.get(i));
            String board = map.get(GameTreeNode.NodeProperties.BOARDSTATE);
            try {
                Integer.parseInt(map.get(GameTreeNode.NodeProperties.MULTIPLICITY));
            } catch (NumberFormatException e) {
                System.err.println("Error Fetching");
            }
            GameTreeNode next = new GameTreeNode(children.get(i));
            BoardState boarstate = new BoardState(board);
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
}
