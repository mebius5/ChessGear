package com.chessgear.server;

import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeBuilder;
import com.chessgear.data.PGNParser;
import com.chessgear.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * User object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class User {

    private GameTree gameTree;
    private List<Game> games;

    private String username;
    private String password;

    /**
     * Creates a user.
     * @param username Username.
     * @param password Password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.gameTree = new GameTree();
        this.games = new ArrayList<>();
    }

    /**
     * Accessor for user's game tree.
     * @return Gametree of user.
     */
    public GameTree getGameTree() {
        return this.gameTree;
    }

    /**
     * Accessor for user's list of games.
     * @return List of games for user.
     */
    public List<Game> getGameList() {
        return this.games;
    }

    /**
     * Parses and adds a game in pgn form.
     * @param pgn PGN game string.
     */
    public void addGame(String pgn) throws Exception {
        PGNParser parser = new PGNParser(pgn);
        GameTreeBuilder treeBuilder = new GameTreeBuilder(parser);
        this.gameTree.addGame(treeBuilder.getListOfNodes());
        Game newGame = new Game(parser);
        this.games.add(newGame);
    }
    
    /***
     * Sets the gameTree to tree
     * @param tree the gameTree to be set to
     */
    public void setGameTree(GameTree tree) {
        this.gameTree = tree;
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
        PASSWORD
    }


}
