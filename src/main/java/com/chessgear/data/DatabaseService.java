

package com.chessgear.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.chessgear.game.Game;
import com.chessgear.server.User;
import com.chessgear.server.User.Properties;

import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

/**
 * @author gilbert
 * 
 * Superkey of an user is it's e-mail adress
 * 
 * This class doesn't build user neither does it handle who is logged or encryption properties.
 * 
 * A class with prewritten querries to the database. Will work on a specified database.
 */
public class DatabaseService {
    
    private final Sql2o database;    
    private final String databasePath;
    
    /**
     * Construct an easy-to-use representation of the database specified by the file.
     * 
     * @param databasePath The path of the database file
     * 
     * @throws IOException If the database file does not exist. 
     * @throws IllegalArgumentException If the database file does not have the specified tables 
     */
    public DatabaseService(String databasePath) throws IOException, IllegalArgumentException{
        this.databasePath = databasePath;
        this.database = prepareCuteDatabase(databasePath);
    }
    
    private Sql2o prepareCuteDatabase(String databasePath){
        //if file does not exists, create it
        Path dbPath = Paths.get(databasePath + "chessgear.sql");
        if (!(Files.exists(dbPath))) {
            Files.createFile(dbPath);
        }

        //create the source database object
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + databasePath + "chessgear.sql");
        
        Sql2o toReturn = new Sql2o(source);
        
        
        //Create the schema for the database if necessary. This allows this
        //program to mostly self-contained. But this is not always what you want;
        //sometimes you want to create the schema externally via a script.
        try (Connection conn = db.open()) {
            String sql = "CREATE TABLE IF NOT EXISTS item (item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "                                 title TEXT, done BOOLEAN, created_on TIMESTAMP)" ;
            conn.createQuery(sql).executeUpdate();
        } catch(Sql2oException ex) {
            logger.error("Failed to create schema at startup", ex);
            throw new TodoServiceException("Failed to create schema at startup", ex);
        }
        
        
        
        
    }
    
    
    

    /**
     * Adds an use to the database
     * 
     * @param email
     * @param attributes
     * @throws IllegalArgumentException
     */
    public void addUser(String email, Map<Properties,String> attributes) throws IllegalArgumentException{
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes an user to the database
     * 
     * @param email
     * @throws IllegalArgumentException
     */
    public void deleteUser(String email) throws IllegalArgumentException{
        throw new UnsupportedOperationException();   
    }

    /**
     * Updates the property of an user 
     * 
     * @param email
     * @param p
     * @param v
     * 
     * @throws IllegalArgumentException
     */
    public void updateUserProperty(String email, Properties p, String v) throws IllegalArgumentException{
        throw new UnsupportedOperationException(); 
    }

    /**
     * This method looks in the database if the user specified by it's e-mail exists.
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @return True if the user exists in the library, else false.
     * 
     * @throws IllegalArgumentException If string email is null
     */
    public boolean userExists(String email) throws IllegalArgumentException{
        throw new UnsupportedOperationException();
    }

    /**
     * This method fetches all the property of the user specified by it's email
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @return A map of all properties for the user.
     * 
     * @throws IllegalArgumentException If the user does not exist see {@link #userExists(String) userExists}
     */
    public Map<Properties, String> fetchUserProperties(String email) throws IllegalArgumentException{
        if(!userExists(email))
            throw new IllegalArgumentException();

        throw new UnsupportedOperationException();
    }

    public void addGame(User u, Game g){

    }

    /**
     * This method builds the database. It specify tables, entities, keys and so on.
     * 
     * @param databasePath The path of the database file wanted
     */
    public static void createDatabaseFile(String databasePath){
        //if file does not exists, create it
        Path dbPath = Paths.get(databasePath + "chessgear.sql");
        if (!(Files.exists(dbPath))) {
            Files.createFile(dbPath);
        }

        //create the source database object
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + databasePath + "chessgear.sql");
        
        
        Sql2o = new Sql2o(dataSource);

        //Create the schema for the database if necessary. This allows this
        //program to mostly self-contained. But this is not always what you want;
        //sometimes you want to create the schema externally via a script.
        try (Connection conn = db.open()) {
            String sql = "CREATE TABLE IF NOT EXISTS item (item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "                                 title TEXT, done BOOLEAN, created_on TIMESTAMP)" ;
            conn.createQuery(sql).executeUpdate();
        } catch(Sql2oException ex) {
            logger.error("Failed to create schema at startup", ex);
            throw new TodoServiceException("Failed to create schema at startup", ex);
        }
        
        
        
        
    }


    /**
     * This method creates a GameTree for the player in the database
     * 
     * @param email The unique identifier of the player
     * @param rootID The unique ID of the root (has only to be unique across nodes of the user)
     */
    public void addTree(String email, int rootID){
        /*
         * IDEA:
         * 
         * insert into Tree
         *    values ('email', rootID) 
         */
    }

    /**
     * This method adds ad child node to a node
     * 
     * @param email The unique identifier of the player
     * @param parentID The unique ID of the parent (has only to be unique across nodes of the user)
     * @param childID The unique ID of the child (has only to be unique across nodes of the user)
     */
    public void addChild(String email, int parentID, int childID){
        /*
         * IDEA:
         * 
         * insert into Node
         *    values ('email', parentID, childID)
         */
    }

    /**
     * Get the root id of the user's game tree
     * 
     * @param email The unique identifier of the player
     * 
     * @return The id of player game tree's roote.
     */
    public int getRoot(String email){
        /*
         * IDEA:
         * 
         * select T.rootid
         * from Tree as T
         * where T.email = 'email'
         * 
         */

        throw new UnsupportedOperationException();
    }

    /**
     * This methods list the children of a node
     * 
     * @param email The unique identifier of the player
     * @param parentID The unique ID of the parent (has only to be unique across nodes of the user)
     * 
     * @return a list of children id
     */
    public List<Integer> childrenFrom(String email, int parentID){
        /*
         * IDEA:
         * 
         * select N.Children
         * Nodes as N
         * where N.PlayerID = PlayerID and N.Node = parentID
         */
        throw new UnsupportedOperationException();
    }

    /**
     * This method finds the parent of a child node.
     * 
     * @param email The unique identifier of the player
     * @param childID The unique ID of the child (has only to be unique across nodes of the user)
     * 
     * @return the node from the parent
     */
    public int parentFrom(String email, int childID){
        /*
         * IDEA:
         * 
         * select
         * Nodes as N
         * where N.PlayerID = playerID and N.Children = childID
         */
        throw new UnsupportedOperationException();
    }
}
