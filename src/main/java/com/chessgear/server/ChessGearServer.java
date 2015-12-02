package com.chessgear.server;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeNode;
import com.chessgear.game.BoardState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object representation of the chessgear server.
 * Created by Ran on 10/8/2015.
 */
public class ChessGearServer {
    /**
     * Constructor with an Empty List
     */
    public ChessGearServer() {
        users = new ArrayList<>();
    }

    /**
     * Adding a user to the stored user Bases
     * @param user the user to be stored
     * @param db the database that will be modified
     */
    public void addOnlineUser(User user, DatabaseService db) {
        String email = user.getEmail();
        int rootid = db.getRoot(email);

        Map<GameTreeNode.NodeProperties, String> map = new HashMap<>();
        String board;
        try {
            map = db.fetchNodeProperty(email, rootid);
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
        makeTree(root, db, email);
        users.add(user);
    }
    //Makes a tree from the Database with a root node
    public int makeTree(GameTreeNode base, DatabaseService db, String email) {
        List<Integer> children;
        try {
            children = db.childrenFrom(email, base.getId());
        } catch(IllegalArgumentException e) {
            return 0;
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
            makeTree(next, db, email);
        }
        return 1;
    }
    /**
     * Removes a user when we no longer need its memory
     * @param email the user to be logged out
     * @param db the database that will be modified
     */
    public void logOutUser(String email, DatabaseService db) {
        User temp = null;
        for (int i = 0; i < users.size(); i++) {
            if (email.equals(users.get(i).getEmail())) {
                temp = users.get(i);
                users.remove(i);
            }
        }
        GameTree tree = temp.getGameTree();
        tree.getRoot().getId();
    }
    /**
     * Gets a User from an email address
     * @param email email address of user requested
     * @return the requrested user with the same email address
     */
    public User getUser(String email) {
        for (int i = 0; i < users.size(); i++) {
            if (email.equals(users.get(i).getEmail())) {
                return users.get(i);
            }
        }
        return null;
    }

    /**
     * List of users.
     */
    private List<User> users;

}
