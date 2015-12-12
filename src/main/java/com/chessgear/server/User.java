package com.chessgear.server;

import com.chessgear.data.*;
import com.chessgear.game.Game;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * User object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class User {

    private GameTree gameTree;    // in the end, this field should be final
    private final List<Game> games;
    private final String username;
    
    //the properties of the user.
    private String password;
    //if wanted, can add private String email here.
    
    //those two field are just references to to shorten the code.
    private static DatabaseService db = DatabaseService.getInstanceOf();
    private static FileStorageService fss = FileStorageService.getInstanceOf();

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(User.class);


    //in the end, only this constructor should remain

    /***
     * Private constructor to create a user
     * @param username the username of the user
     */
    private User(String username){
        this.username = username;
        this.games = new ArrayList<>();
    }
    
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
     * Parses and adds a game in pgn form. The String will be stored as a file. If you want to choose the 
     * name, please use the method addGame(String pgn, String fileName). The default name is 
     * username_&lt;current time&gt;.pgn
     * 
     * @param pgn A PGN representing the game
     */
    public void addGame(String pgn){
        //make a mokeupname
        String name = username + "_" + UUID.randomUUID().toString() + ".pgn";
        addGame(pgn, name);
    }

    /**
     * Parses and adds a game in pgn form. The String will be stored as a file.
     * 
     * @param pgn A string representing the game in PGN format.
     * @param fileName The name of the file.
     */
    public void addGame(String pgn, String fileName){
        //first, we store safely the file
        try {
            fss.addFile(username, fileName, pgn);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Was not able to store file " + fileName + " for user " + username);
        }
        
        //now we add the game to the list of game and to the tree
        try {
            PGNParser parser = new PGNParser(pgn);
            GameTreeBuilder treeBuilder = new GameTreeBuilder(parser);
            Game newGame = new Game(parser);
            
            //we modify the user's belonging.
            gameTree.addGame(treeBuilder.getListOfNodes(), username);
            games.add(newGame);
        } catch (PGNParseException e) {
            e.printStackTrace();
            logger.error("Was not able to parse the file "+ fileName + " for user "+username);
        } catch (Exception e) {
            //TODO: check this, really ugly Exception.
            e.printStackTrace();
        } 
    }
    
    /***
     * DEPRECATED: modify directly the GameTree, not it's reference!
     *
     * Sets the gameTree to tree
     * //@param //tree the gameTree to be set to
     */
    @Deprecated
    /***
    public void setGameTree(GameTree tree) {
        this.gameTree = tree;
    }
     ***/
    
    /**
     * Sets the password of an user, makes the necessary calls to update the database.
     * 
     * @param newPassword The new password.
     */
    public void setPassword(String newPassword){
        this.password = newPassword;
        
        //make the changes in the database
        db.updateUserProperty(username, Property.PASSWORD, newPassword);
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
     * This methods creates a new user, (the first time he registers). It makes the necessary calls to update the database.
     * Initially, this user has no game and an empty GameTree.
     * 
     * @param username The name of the user
     * @param password The password of the user
     * 
     * @return An instance representing this user, for commodity.
     */
    public static User registerNewUser(String username, String password){
        if (db.userExists(username))
            throw new IllegalArgumentException("user already exists");
        
        User user = new User(username, password);
        DatabaseWrapper.getInstance().addUser(user);
        return user;
    }
    
    /**
     * This methods fetches the database and creates a handy representation of user.
     * Some call should be made to contol the validity of the password after that.
     * 
     * @param username The name of the user.
     * 
     * @return A handy representation of an User.
     */
    public static User getUser(String username){
        logger.info("Building user " + username + " from database!");

        if(!db.userExists(username))
            throw new IllegalArgumentException("user does not exist in the database");
        
        // first we fetch basic data.
        User toReturn = new User(username);
        toReturn.password = db.fetchUserProperties(username).get(Property.PASSWORD);
        
        // now we reconstruct its gametree
        toReturn.gameTree = DatabaseWrapper.getInstance().getGameTree(username);
        
        //we reconstruct the list of all it's games
        for(String filename: fss.getFilesFor(username)){
            try {
                String fileContent;
                fileContent = fss.fetchFileContent(username, filename);
                toReturn.games.add(new Game(new PGNParser(fileContent)));
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Was not able to fetch content of file "+filename+" of user "+username);
            } catch (PGNParseException e) {
                e.printStackTrace();
                logger.error("Was not able to parse the content file "+filename+" of user "+username);
            }
        }

        return toReturn;
    }

    /**
     * This enum is the contract for the DatabaseService. It represents all the fields that should be stored for an user. For instance
     * if it contains property1 and property2, the fields (username, property1, property2) will be stored for every user. This permits
     * the database to be abstracted of what data we want to store.
     */
    public enum Property {
        PASSWORD;

        /**
         * Creates the mapping between enum and values for the given user.
         * @param user User for whom to construct the mapping.
         * @return Mapping between enum and values.
         */
        public static Map<Property, String> getProperties(User user) {
            HashMap<Property, String> result = new HashMap<>();
            result.put(PASSWORD, user.getPassword());
            return result;
        }
    }

    /**
     * Gets the Json representation of this.
     * @return the Json string representation of the games
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
