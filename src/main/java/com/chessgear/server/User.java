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

    public enum Property {
        EMAIL, USERNAME, PASSWORD
    }

}
