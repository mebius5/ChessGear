package com.chessgear.server;

import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeBuilder;
import com.chessgear.data.PGNParser;
import com.chessgear.game.Game;
import com.google.gson.GsonBuilder;

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
     * Gets game by id.
     * @param id Id of game we want.
     * @return Game with corresponding id.
     */
    public Game getGameById(int id) {
        for (Game g : this.games) {
            if (g.getID() == id) return g;
        }

        return null;
    }

    /**
     * Parses and adds a game in pgn form.
     * @param pgn PGN game string.
     */
    public void addGame(String pgn) throws Exception {
        PGNParser parser = new PGNParser(pgn);
        GameTreeBuilder treeBuilder = new GameTreeBuilder(parser);
        this.gameTree.addGame(treeBuilder.getListOfNodes());
        Game newGame = new Game(parser, Game.getNextGameId());
        System.out.println("Game id : " + newGame.getID());
        this.games.add(newGame);
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
     * Mutator for password.
     * @param password New password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * List of the User Properties
     */
    public enum Property {
        USERNAME, PASSWORD
    }

    /**
     * Mutator for GameTree.
     * @param tree
     */
    public void setGameTree(GameTree tree) {
        this.gameTree = tree;
    }

    /**
     * Gets the Json representation of this.
     * @return
     */
    public String getGamesJson() {
        return new GsonBuilder().serializeNulls().create().toJson(new UserGamesJson(this));
    }

    /**
     * Container class for converting user's list of games to json.
     */
    private static class UserGamesJson {

        private List<GameJson> games;

        public UserGamesJson(User user) {
            this.games = new ArrayList<>();
            for (Game g : user.games) {
                this.games.add(new GameJson(g));
            }
        }

    }

    /**
     * Container class for converting information about games to JSON.
     */
    private static class GameJson {

        private String name;
        private int id;

        public GameJson(Game game) {
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(game.getResult().toString());
            nameBuilder.append(" - ");
            nameBuilder.append(game.getWhitePlayerName());
            nameBuilder.append(" vs ");
            nameBuilder.append(game.getBlackPlayerName());
            nameBuilder.append(", ");
            nameBuilder.append(game.getDateImported().toString());
            this.name = nameBuilder.toString();
            this.id = game.getID();
        }

    }
}
