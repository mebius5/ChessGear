package com.chessgear.server;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.FileStorageService;
import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeBuilder;
import com.chessgear.data.GameTreeNode;
import com.chessgear.data.PGNParseException;
import com.chessgear.data.PGNParser;
import com.chessgear.game.BoardState;
import com.chessgear.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class User {

    private GameTree gameTree;    //in the end, this field should be final
    private final List<Game> games;
    private final String username;
    
    //the properties of the user.
    private String password;
    //if wanted, can add private String email here.
    
    //those two field are just references to to shorten the code.
    private static final DatabaseService db = DatabaseService.getInstanceOf();
    private static final FileStorageService fss = FileStorageService.getInstanceOf();
    
    //in the end, only this constructor should remain
    private User(String username){
        this.username = username;
        this.games = new ArrayList<>();
    }
    
    /**
     * DEPRECATED: if you want to fetch back an existing user from the database, use getUser(String username) function.
     *             if you want to register a new user (inexistant in the database, use registerUser(String username) function.
     * 
     * Creates a user.
     * @param username Username.
     * @param password Password.
     */
    @Deprecated
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
     * username_<current time>.pgn
     * 
     * @param pgn A PGN representing the game
     */
    public void addGame(String pgn){
        //make a mokeupname
        String name = username + "_" + System.currentTimeMillis() + ".pgn";
        
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
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Was not able to store file " + fileName + " for user " + username);
        }
        
        //now we add the game to the list of game and to the tree
        try {
            PGNParser parser = new PGNParser(pgn);
            GameTreeBuilder treeBuilder = new GameTreeBuilder(parser);
            Game newGame = new Game(parser);
            
            //we modify the user's belonging.
            gameTree.addGame(treeBuilder.getListOfNodes());
            games.add(newGame);
        } catch (PGNParseException e) {
            e.printStackTrace();
            System.err.println("Was not able to parse the file "+ fileName + " for user "+username);
        }
        catch (Exception e) {
            //TODO: check this, really ugly Exception.
            e.printStackTrace();
        } 
    }
    
    /***
     * DEPRECATED: modify directly the GameTree, not it's reference!
     * 
     * Sets the gameTree to tree
     * @param tree the gameTree to be set to
     */
    @Deprecated
    public void setGameTree(GameTree tree) {
        this.gameTree = tree;
    }
    
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
     * This methods creates a new user, (the first time he registers). It makes the necessary calls to update the database.
     * Initially, this user has no game and an empty GameTree.
     * 
     * @param username The name of the user
     * @param password The password of the user
     * 
     * @return An instance representing this user, for commodity.
     */
    public static User registerNewUser(String username, String password){
        if(db.userExists(username))
            throw new IllegalArgumentException("user already exists");
        
        User toReturn = new User(username);
        toReturn.password = password;
        toReturn.gameTree = new GameTree();
        
        //now we store all those info in the database.
        HashMap<Property, String> userProp = new HashMap<>();
        userProp.put(Property.PASSWORD, password);
        db.addUser(username, userProp);
        
        return toReturn;
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
        if(!db.userExists(username))
            throw new IllegalArgumentException("user does not exist in the database");
        
        //first we fetch basic data.
        User toReturn = new User(username);
        toReturn.password = db.fetchUserProperties(username).get(Property.PASSWORD);
        
        //now we reconstruct it's gametree
        toReturn.gameTree = GameTreeBuilder.constructGameTree(username);
        
        //we reconstruct the list of all it's games
        for(String filename: fss.getFilesFor(username)){
            try {
                String fileContent ;
                fileContent = fss.fetchFileContent(username, filename);
                toReturn.games.add(new Game(new PGNParser(fileContent)));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Was not able to fetch content of file "+filename+" of user "+username);
            } catch (PGNParseException e) {
                e.printStackTrace();
                System.err.println("Was not able to parse the content file "+filename+" of user "+username);
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
        PASSWORD
    }
    
}
