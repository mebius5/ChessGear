package com.chessgear.server;

import com.chessgear.data.DatabaseService;

import java.util.HashMap;

/**
 * ChessGear server object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class Server {

    private HashMap<String, User> users;

    /**
     * Initializes a server.
     */
    public Server() {
        this.users = new HashMap<>();
    }

    /**
     * Adds user.
     * @param user User to add.
     */
    public void addUser(User user) {
        this.users.put(user.getUsername(), user);
    }

    /**
     * Checks if a user with specified username exists.
     * @param username Username to check.
     * @return True if already exists, false if doesn't.
     */
    public boolean userExists(String username) {
        if (this.users.containsKey(username)) {
            return true;
        } else {
            // Else check server
            if (DatabaseService.getInstanceOf().userExists(username)) {
                this.users.put(username, User.getUser(username));
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the user with specified username.
     * @param username Username of user to retrieve.
     * @return Gets object if exists, else null.
     */
    public User getUser(String username) {
        if (this.userExists(username)) return this.users.get(username);
        return null;
    }
}
