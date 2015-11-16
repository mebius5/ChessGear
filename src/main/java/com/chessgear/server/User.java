package com.chessgear.server;

import com.chessgear.data.GameTree;
import com.chessgear.game.Game;

import java.util.List;

/**
 * User class.
 * Created by Ran on 10/8/2015.
 */
public class User {
    /**
     * Constructor
     */
    public User(String usernam, String em, String passw) {
        username = usernam;
        email = em;
        password = passw;
    }
    /**
     * User's username.
     */
    private String username;
    /**
     * User's email.
     */
    private String email;
    /**
     * User's password.
     */
    private String password;

    /**
     * User's tree of games.
     */
    private GameTree gameTree;

    /**
     * List of user's games.
     */
    private List<Game> games;

    /**
     * Getting the username
     */
    public String getEmail() {
        return email;
    }
    public enum Property {
        USERNAME, PASSWORD
    }

    /**
     * Setting the game tree
     */
    public void setGameTree(GameTree tree) {
        gameTree = tree;
    }
}
