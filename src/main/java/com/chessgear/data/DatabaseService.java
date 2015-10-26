package com.chessgear.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chessgear.game.Game;
import com.chessgear.server.User;
import com.chessgear.server.User.Property;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

/**
 * @author gilbert
 * 
 * Superkey of an user is it's e-mail adress
 * 
 * This class doesn't build user neither does it handle who is logged or encryption properties.
 */
public class DatabaseService {

    private static final String CANONICAL_DB_NAME = "chessgear.sql";

    private final Sql2o database;    
    private final String databasePath;

    /**
     * Construct an easy-to-use representation of the database. If the database does not already exists,
     * calling this constructor will build one.
     * 
     * @param prefix The path of the database file or a simple prefix for test purpose
     * 
     * @throws IOException If something gets bad with the database file
     * @throws IllegalArgumentException If the prefix is null.
     */
    public DatabaseService(String prefix) throws IOException, IllegalArgumentException{
        if(prefix == null)
            throw new IllegalArgumentException();
        
        this.databasePath = prefix;
        this.database = prepareCuteDatabase(prefix);
    }

    private Sql2o prepareCuteDatabase(String databasePath) throws IOException{        
        //if file does not exists, create it
        Path dbPath = Paths.get(databasePath + CANONICAL_DB_NAME);
        if (!(Files.exists(dbPath))) {
            Files.createFile(dbPath);
        }

        //create the source database object
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + databasePath + "chessgear.sql");

        Sql2o toReturn = new Sql2o(source);
        
        //Schema for user table
        StringBuilder builderUserSpec = new StringBuilder("CREATE TABLE IF NOT EXISTS User(email TEXT PRIMARY KEY");
        for(User.Property P : User.Property.values())
            builderUserSpec.append(", "+P.name().toLowerCase()+" TEXT");
        builderUserSpec.append(")");
        String userSpec = builderUserSpec.toString();
        
        /*
         *  create more specifications here!
         */
        
        //Create the schema for the database if necessary.
        Connection conn = toReturn.open();
        conn.createQuery(userSpec).executeUpdate();
        
        /*
         * execute more creating table querry here!
         */
        
        conn.close();

        return toReturn;
    }

    /**
     * WARNING: ONLY FOR TESTING PURPOSE
     * 
     * @throws IOException 
     */
    public void eraseDatabaseFile() throws IOException{
        Files.delete(Paths.get(databasePath + CANONICAL_DB_NAME));
    }

    /**
     * Adds an user to the database.
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!).
     * @param attributes The attributes of the user. If some is non-specified, NULL value will be put in the database.
     * 
     * @throws IllegalArgumentException If the e-mail is null, already exists or the set of attributes is null.
     */
    public void addUser(String email, Map<Property,String> attributes) throws IllegalArgumentException{
        if(email == null || attributes == null)
            throw new IllegalArgumentException("args should not be null");
        
        if(userExists(email))
            throw new IllegalArgumentException("User with same superkey already exists");
        
        //constructing the sql command
        StringBuilder cmdBuilder = new StringBuilder("INSERT INTO User Values('"+email+"'");
        for(User.Property P : User.Property.values()){
            cmdBuilder.append(", ");
            
            //if property not availaible, should put NULL into database
            if(!attributes.containsKey(P))
                cmdBuilder.append("NULL");
            else
                cmdBuilder.append("'" + attributes.get(P) + "'");
        }
        cmdBuilder.append(");");
        String cmd = cmdBuilder.toString();
        
        Connection conn = database.open();
        conn.createQuery(cmd).executeUpdate();
        conn.close();
    }

    /**
     * Deletes an user in the database
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @throws IllegalArgumentException If the specified user does not exist in the database
     */
    public void deleteUser(String email) throws IllegalArgumentException{        
        if(!userExists(email))
            throw new IllegalArgumentException("user does not exists");
        
        String cmd = "DELETE FROM User Where email='"+email+"';";
        Connection conn = database.open();
        conn.createQuery(cmd).executeUpdate();
        conn.close(); 
    }

    /**
     * Updates the a field of the property of an user.
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!).
     * @param p The field to modify.
     * @param v The value to put in place.
     * 
     * @throws IllegalArgumentException If the user does not exists in the database or if the value is null.
     */
    public void updateUserProperty(String email, Property p, String v) throws IllegalArgumentException{
        if(!userExists(email))
            throw new IllegalArgumentException("specified user does not exists in database");
        
        if(v == null)
            throw new IllegalArgumentException("args should not be null");
        
        String cmd = "UPDATE User SET "+p.toString().toLowerCase()+"='"+v+"' WHERE email = '"+email+"';";
        
        Connection conn = database.open();
        conn.createQuery(cmd).executeUpdate();
        conn.close();
    }

    /**
     * This method looks in the database if the user specified by it's e-mail exists.
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @return True if the user exists in the library, else false. Also if null value is provided, return false.
     */
    public boolean userExists(String email){
        if(email == null)
            return false;
        
        String cmd = "SELECT S.email FROM User as S where S.email = '"+email+"'";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
        
        return !boh.isEmpty();
    }

    /**
     * This method fetches all the property of the user specified by it's email
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @return A map of all properties and their associated value for the user. If no value was specified for 
     * a property, then the corresponding value will simply be null. 
     * 
     * @throws IllegalArgumentException If the user does not exist see {@link #userExists(String) userExists}
     */
    public Map<Property, String> fetchUserProperties(String email) throws IllegalArgumentException{        
        if(!userExists(email))
            throw new IllegalArgumentException("The user does not exists");
        
        String cmd = "SELECT * FROM User as S where S.email = '"+email+"'";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
                
        HashMap<Property, String> toReturn = new HashMap<>();
        
        for(Property pr: Property.values()){
            //has to be flexiple with NULL values
            String value = (boh.get(0).get(pr.toString().toLowerCase()) == null)? 
                    null :
                    boh.get(0).get(pr.toString().toLowerCase()).toString();
            
            toReturn.put(pr, value);
        }

        return toReturn;
    }

    public void addGame(User u, Game g){

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
