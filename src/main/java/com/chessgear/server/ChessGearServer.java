package com.chessgear.server;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeNode;
import com.chessgear.game.BoardState;

import javax.xml.crypto.Data;
import java.util.*;

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
     * Store tree
     */
    public void storeTree(String email, GameTree n, DatabaseService db) {
        //insert here
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
        HashMap<Integer, GameTreeNode> nodemapping = makeTree(root, db, email);
        GameTree tree = new GameTree();
        tree.setNodeMapping(nodemapping);
        tree.setRoot(root);
        user.setGameTree(tree);
        System.out.println("Adding " + user.getEmail());
        users.add(user);
        System.out.println(users.size());
    }
    //Makes a tree from the Database with a root node
    public HashMap<Integer, GameTreeNode> makeTree(GameTreeNode base, DatabaseService db, String email) {
        List<Integer> children;
        HashMap<Integer, GameTreeNode> nodemapping = new HashMap<>();
        System.out.println("hey");
        try {
            children = db.childrenFrom(email, base.getId());
        } catch(IllegalArgumentException e) {
            nodemapping.put(base.getId(), base);
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
            nodemapping = makeTree(next, db, email);

        }
        nodemapping.put(base.getId(), base);
        return nodemapping;
    }
    /**
     * Removes a user when we no longer need its memory
     * @param email the user to be logged out
     * @param db the database that will be modified
     */
    public void logOutUser(String email, DatabaseService db) {
        User temp = null;
        int rootid = db.getRoot(email);
        temp = getUser(email);
        GameTree tree = temp.getGameTree();
        deleteTree(email, db, rootid);
        storeTree(email, tree, db, rootid);
        for (int i = 0; i < users.size(); i++) {
            if (email.equals(users.get(i).getEmail())) {
                temp = users.get(i);
                users.remove(i);
            }
        }
    }
    /**
     * For Storing the tree
     */
    public int storeTree(String email, GameTree gametree, DatabaseService db,int id) {
        GameTreeNode curr = gametree.getNodeWithId(id);
        List<GameTreeNode> children = curr.getChildren();
        try {
            for(int i = 0;i < children.size(); i++) {
                GameTreeNode temp = children.get(i);
                storeTree(email, gametree, db, temp.getId());
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
     * For deleting hte tree
     * @param email
     * @param db
     * @param id
     * @return
     */
    public int deleteTree (String email, DatabaseService db, int id) {
        List<Integer> children;
        try{
            children = db.childrenFrom(email, id);
        } catch (IllegalArgumentException e) {
            try {
                db.deleteNode(email, id);
            } catch (IllegalArgumentException b) {
                return 0;
            }
            return 0;
        }
        for (int i = 0; i < children.size(); i++) {
            deleteTree(email, db, children.get(i));
        }
        db.deleteNode(email, id);
        return 1;
    }
    /**
     * Gets a User from an email address
     * @param email email address of user requested
     * @return the requrested user with the same email address
     */
    public User getUser(String email) {
        System.out.println(users.size());
        for (int i = 0; i < users.size(); i++) {
            System.out.println(email + " " + users.get(i).getEmail());
            if (email.equals(users.get(i).getEmail())) {

                return users.get(i);
            }
        }
        return null;
    }

    /**
     * List of users.
     */
    private ArrayList<User> users;

}
