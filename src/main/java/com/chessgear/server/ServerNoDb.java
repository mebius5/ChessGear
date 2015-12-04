package com.chessgear.server;

import java.util.HashMap;

/**
 * ChessGear server object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class ServerNoDb {

    private HashMap<String, UserNoDb> users;

    /**
     * Initializes a server.
     */
    public ServerNoDb() {
        this.users = new HashMap<>();
    }

    /**
     * Adds user.
     * @param user User to add.
     */
    public void addUser(UserNoDb user) {
        this.users.put(user.getUsername(), user);
    }

    /**
     * Checks if a user with specified username exists.
     * @param username Username to check.
     * @return True if already exists, false if doesn't.
     */
    public boolean userExists(String username) {
        return this.users.containsKey(username);
    }

    /**
     * Gets the user with specified username.
     * @param username Username of user to retrieve.
     * @return Gets object if exists, else null.
     */
    public UserNoDb getUser(String username) {
        if (this.userExists(username)) return this.users.get(username);
        return null;
    }
}
