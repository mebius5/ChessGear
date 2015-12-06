package com.chessgear.server;

import com.chessgear.data.GameTree;
import com.chessgear.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * User class.
 * Created by Ran on 10/8/2015.
 */
public class User {

    /**
     * Default constructor
     * @param usernam the username of the User
     * @param em the email of the User
     * @param passw the password of the User
     */
    public User(String usernam, String em, String passw) {
        username = usernam;
        email = em;
        password = passw;
        games = new ArrayList<>();
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
     * Number of games
     */
    private int numgames;
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
     * Getting the email of the User
     * @return the email of the User
     */
    public String getEmail() {
        return email;
    }

    /**
     * List of the User Properties
     */
    public enum Property {
        USERNAME, PASSWORD
    }

    /**
     * Adding a game
     * @param game the game
     */
    public void addGame(Game game) {
        this.games.add(game);
        this.numgames++;
    }

    /**
     * returning the games
     * @return the num of games
     */
    public int getNumgames() {
        return numgames;
    }
    /**
     * Way to get the tree
     * @return the gameTree of the User
     */
    public GameTree getGameTree() { return gameTree; }
    /**
     * Setting the game tree
     * @param tree set the gameTree of the User to tree
     */
    public void setGameTree(GameTree tree) {
        this.gameTree = tree;
    }
}
