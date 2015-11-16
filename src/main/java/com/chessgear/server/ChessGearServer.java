package com.chessgear.server;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.GameTreeNode;

import java.util.ArrayList;
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
     * Adding a user to the cached user
     * @param user the user to be stored
     */
    public void addOnlineUser(User user, DatabaseService db) {
        String email = user.getEmail();
        int rootid = db.getRoot(email);
        Map<GameTreeNode.NodeProperties, String> map = db.fetchNodeProperty(email, rootid);

        users.add(user);

    }

    /**
     * Removes a user when we no longer need its memory
     * @param user the user to be logged out
     */
    public void logOutUser(User user) {
        users.remove(user);
    }
    /**
     * List of users.
     */
    private List<User> users;

}
