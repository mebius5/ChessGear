package com.chessgear.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chessgear.data.GameTreeNode.NodeProperties;
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
        
        //Schema for user table: User - (email, ... properties ...)
        StringBuilder builderUserSpec = new StringBuilder("CREATE TABLE IF NOT EXISTS User(email TEXT PRIMARY KEY");
        for(User.Property P : User.Property.values())
            builderUserSpec.append(", "+P.name().toLowerCase()+" TEXT");
        builderUserSpec.append(")");
        String userSpec = builderUserSpec.toString();
        
        //Schema for nodes tables: Node - (email, nodeId, ... properties ...)        
        StringBuilder builderNodeSpec = new StringBuilder("CREATE TABLE IF NOT EXISTS Node(email TEXT, nodeId INTEGER");
        for(GameTreeNode.NodeProperties P : GameTreeNode.NodeProperties.values())
            builderNodeSpec.append(", "+P.name().toLowerCase()+" TEXT");
        builderNodeSpec.append(", PRIMARY KEY (email, nodeId))");
        String nodeSpec = builderNodeSpec.toString();
        
        //Schema for table storing trees: Tree - (email, rootId)
        String treeSpec = "CREATE TABLE IF NOT EXISTS Tree(email TEXT PRIMARY KEY, rootNodeId INTEGER)";
        
        //Schema for nodes relation: ParentOf - (email, parentId, childId) -- remember, in a tree child has only one parent
        String parentOfSpec = "CREATE TABLE IF NOT EXISTS ParentOf(email TEXT, parentId, childId INTEGER, PRIMARY KEY (email, childId))";
        
        //run all commands
        Connection conn = toReturn.open();
        conn.createQuery(userSpec).executeUpdate();
        conn.createQuery(treeSpec).executeUpdate();
        conn.createQuery(nodeSpec).executeUpdate();
        conn.createQuery(parentOfSpec).executeUpdate();
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

    public boolean nodeExists(String email, int nodeId){
        if(!userExists(email))
            return false;
        
        String cmd = "SELECT * FROM Node as N WHERE N.email = '" + email + "' and N.nodeId = " + nodeId;
        
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
     * @param rootId The unique ID of the root (has only to be unique across nodes of the user)
     */
    public void addTree(String email, int rootId){
        if(nodeExists(email, rootId))
            throw new IllegalArgumentException("Node does not exists in the database");
        
        //check that the user has not already a tree TODO: use SQL trigger mechansim to make it better
        String cmd = "SELECT * FROM Tree as T where T.email = '"+email+"'";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
        
        if(!boh.isEmpty())
            throw new IllegalArgumentException("The user already has a game tree");
        
        //finally execute command
        cmd = "INSERT INTO Tree Values('"+email+"', "+rootId+")";
        
        conn = database.open();
        conn.createQuery(cmd).executeUpdate();
        conn.close();        
    }
    
    /**
     * This methods adds a node to the database. The primary keys of the node are it's email (the user to whom
     * the node belongs to), and nodeId the id of the node (note that the nodeId has only to be unique for a particular
     * user.)
     * 
     * @param email The email of the user to whom the node belongs.
     * @param nodeId The id of the node. (has to be unique for the player)
     * @param properties A Map of properties that the node has (if some attributes are non specified, they will be
     * stored as NULL)
     * 
     * @throws IllegalArgumentException If the node already exists or if the user does not exists.
     */
    public void addNode(String email, int nodeId, Map<GameTreeNode.NodeProperties, String> properties){
        if(nodeExists(email, nodeId))
            throw new IllegalArgumentException("Node already exists in database");
        else if(!userExists(email))
            throw new IllegalArgumentException("User does not exists");
        else if(properties == null)
            throw new IllegalArgumentException("properties cannot be null");

        //constructing the sql command
        StringBuilder cmdBuilder = new StringBuilder("INSERT INTO Node Values('"+email+"', "+nodeId);
        for(GameTreeNode.NodeProperties P : GameTreeNode.NodeProperties.values()){
            cmdBuilder.append(", ");
            
            //if property not availaible, should put NULL into database
            if(!properties.containsKey(P))
                cmdBuilder.append("NULL");
            else
                cmdBuilder.append("'" + properties.get(P) + "'");
        }
        cmdBuilder.append(");");
        String cmd = cmdBuilder.toString();
        
        Connection conn = database.open();
        conn.createQuery(cmd).executeUpdate();
        conn.close();
    }

    /**
     * This method adds a child node to a node
     * 
     * @param email The unique identifier of the player
     * @param parentId The unique ID of the parent (has only to be unique across nodes of the user)
     * @param childId The unique ID of the child (has only to be unique across nodes of the user)
     * 
     * @throws IllegalArgumentException if the child already has a parent. (In a tree, there is only one parent)
     */
    public void addChild(String email, int parentId, int childId){        
        if(!nodeExists(email, parentId) || !nodeExists(email, childId))
            throw new IllegalArgumentException("parent or child not present in database");
        if(parentId == childId)
            throw new IllegalArgumentException("parent cannot be it's own child (and vice-versa)");
        
        
        //check that the child has no parent yet
        String cmd = "SELECT * FROM ParentOf as P WHERE P.childId = "+childId;
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        
        if(!boh.isEmpty())
            throw new IllegalArgumentException("this child already has a parent!");

        //now we can execute the command
        cmd = "INSERT INTO ParentOf Values('"+email+"', "+parentId+", "+childId+")";
        conn.createQuery(cmd).executeUpdate();
        conn.close();   
    }

    /**
     * Get the root id of the user's game tree
     * 
     * @param email The unique identifier of the player
     * 
     * @return The id of player game tree's roote.
     */
    public int getRoot(String email){
        if(!userExists(email))
            throw new IllegalArgumentException("Use does not exists in the database");
        
        
        String cmd = "SELECT rootNodeId FROM Tree as T where T.email = '"+email+"'";
        
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
                
        if(boh.size() == 0)
            throw new IllegalArgumentException("This user has no gametree yet");
  
        return (Integer) boh.get(0).get("rootnodeid");
    }
    
    public Map<GameTreeNode.NodeProperties, String> fetchNodeProperty(String email, int nodeid){

        if(!nodeExists(email, nodeid))
            throw new IllegalArgumentException("The user or node does not exists");
        
        String cmd = "SELECT * FROM Node as N where N.email = '"+email+"' and N.nodeid = '"+nodeid+"'";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
                
        HashMap<NodeProperties, String> toReturn = new HashMap<>();
        
        for(GameTreeNode.NodeProperties pr: GameTreeNode.NodeProperties.values()){
            //has to be flexible with NULL values
            String value = (boh.get(0).get(pr.toString().toLowerCase()) == null)? 
                    null :
                    boh.get(0).get(pr.toString().toLowerCase()).toString();
            
            toReturn.put(pr, value);
        }

        return toReturn;
    }

    /**
     * This methods list the children of a node
     * 
     * @param email The unique identifier of the player
     * @param parentID The unique ID of the parent (has only to be unique across nodes of the user)
     * 
     * @return a list of children id
     */
    public List<Integer> childrenFrom(String email, int parentId){
        if(!nodeExists(email, parentId))
            throw new IllegalArgumentException("Use does not exists in the database");
        
        String cmd = "SELECT childId FROM ParentOf as P where P.email = '"+email+"' and P.parentId = "+parentId;
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
        
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        for(Map<String, Object> m: boh)
            toReturn.add((Integer)m.get("childid"));
        
        return toReturn;
    }

    /**
     * This method finds the parent of a child node.
     * 
     * @param email The unique identifier of the player
     * @param childID The unique ID of the child (has only to be unique across nodes of the user)
     * 
     * @return the node from the parent
     */
    public int parentFrom(String email, int childId){
        if(!nodeExists(email, childId))
            throw new IllegalArgumentException("node does not exists in the database");
        
        String cmd = "SELECT parentId FROM ParentOf as P where P.email = '"+email+"' and P.childId = "+childId;
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).executeAndFetchTable().asList();
        conn.close();
        
        if(boh.isEmpty())
            throw new IllegalArgumentException("this node has no parent! (it's a root node)");
        
        return (Integer) boh.get(0).get("parentid");
    }
}
