package com.chessgear.server;

import com.chessgear.data.GameTree;

/**
 * User object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class UserNoDb {

    private GameTree gameTree;

    private String username;
    private String password;

    /**
     * Creates a user.
     * @param username Username.
     * @param password Password.
     */
    public UserNoDb(String username, String password) {
        this.username = username;
        this.password = password;
        this.gameTree = new GameTree();
    }

    /**
     * Accessor for user's game tree.
     * @return Gametree of user.
     */
    public GameTree getGameTree() {
        return this.gameTree;
    }

    /**
     * Accessor for username.
     * @return Username of user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Accessor for password.
     * @return Password of user.
     */
    public String getPassword() {
        return this.password;
    }
    /**
     * List of the User Properties
     */
    public enum Property {
        USERNAME, PASSWORD
    }

    public void setGameTree(GameTree tree) {
        this.gameTree = tree;
    }
}
