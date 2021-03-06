package com.chessgear.data;

import com.chessgear.data.GameTreeNode.NodeProperties;
import com.chessgear.server.User;
import com.chessgear.server.User.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Here are some important things to keep in mind when using this class: 
 * <ul>
 *  <li> This is a dumb class it does not handle who is logged in or any encryption properties.
 *  <li> It just hides SQL implentation details.
 *  <li> The superkey (unique identifier) of an user is it's username.
 *  <li> The superkey of a node is the combination of it's owner and an unique integer id.
 *  <li> It is SQL-injection safe
 * </ul>
 */
public final class DatabaseService {
    public static final String CANONICAL_DB_NAME = "chessgear.sql";

    private static DatabaseService instance;
    
    private final Sql2o database;    
    private final String databasePath;

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    /**
     * Construct an easy-to-use representation of the database. If the database does not already exists,
     * calling this constructor will build one.
     * 
     * @param prefix The path of the database file or a simple prefix for test purpose
     * 
     * @throws IOException If something gets bad with the database file
     * @throws IllegalArgumentException If the prefix is null.
     */
    private DatabaseService(String prefix) throws IOException, IllegalArgumentException{
        //check that data folder exists:
        File general = new File(FileStorageService.DATA_DIRECTORY_NAME);
        if(!general.exists())
            if(!general.mkdir()){
                IllegalArgumentException e = new IllegalArgumentException("Cannot make directory for "+general.getName());
                logger.error(e.getMessage());
                throw e;
            }
        
        if(prefix == null) {
            IllegalArgumentException e = new IllegalArgumentException("Null prefix for database");
            logger.error(e.getMessage());
            throw e;
        }

        this.databasePath = FileStorageService.DATA_DIRECTORY_NAME + File.separator + prefix + CANONICAL_DB_NAME;
        this.database = prepareCuteDatabase(prefix);
    }

    /**
     * Gets the instance of this singleton class.
     * @return The database.
     */
    public static DatabaseService getInstanceOf(){
        if(instance == null)
            try {
                instance = new DatabaseService("");
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }

        return instance;
    }

    /**
     * Prepares an sql2o object for handling database queries.
     * @param databaseFileName Filename of database.
     * @return Sql2o object for handling queries on the given database file.
     * @throws IOException File not found.
     */
    private Sql2o prepareCuteDatabase(String databaseFileName) throws IOException{        
        //check that data folder exists:
        File general = new File(FileStorageService.DATA_DIRECTORY_NAME);
        if(!general.exists())
            if(!general.mkdir()){
                IOException e = new IOException("Cannot make director for "+general.getName());
                logger.error(e.getMessage());
                throw e;
            }
                
        //create the source database object
        SQLiteDataSource source = new SQLiteDataSource();
        source.setUrl("jdbc:sqlite:" + databasePath);

        Sql2o toReturn = new Sql2o(source);
        
        //Schema for user table: User - (username, ... properties ...)
        StringBuilder builderUserSpec = new StringBuilder("CREATE TABLE IF NOT EXISTS User(username TEXT PRIMARY KEY");
        for(User.Property P : User.Property.values())
            builderUserSpec.append(", "+P.name().toLowerCase()+" TEXT");
        builderUserSpec.append(")");
        String userSpec = builderUserSpec.toString();
        
        //Schema for nodes tables: Node - (username, nodeId, ... properties ...)        
        StringBuilder builderNodeSpec = new StringBuilder("CREATE TABLE IF NOT EXISTS Node(username TEXT, nodeId INTEGER");
        for(GameTreeNode.NodeProperties P : GameTreeNode.NodeProperties.values())
            builderNodeSpec.append(", "+P.name().toLowerCase()+" TEXT");
        builderNodeSpec.append(", PRIMARY KEY (username, nodeId))");
        String nodeSpec = builderNodeSpec.toString();
        
        //Schema for table storing trees: Tree - (username, rootId)
        String treeSpec = "CREATE TABLE IF NOT EXISTS Tree(username TEXT PRIMARY KEY, rootNodeId INTEGER)";
        
        //Schema for nodes relation: ParentOf - (username, parentId, childId) -- remember, in a tree child has only one parent
        String parentOfSpec = "CREATE TABLE IF NOT EXISTS ParentOf(username TEXT, parentId, childId INTEGER, PRIMARY KEY (username, childId))";
        
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
     * Only for testing.
     * 
     * @throws IOException if eraseDatabaseFile fails
     */
    @SuppressWarnings("unused")
    private void eraseDatabaseFile() throws IOException{
        if(Files.exists(Paths.get(databasePath))) {
            Files.delete(Paths.get(databasePath));
            logger.info("Deleting " + databasePath);
        }
    }

    /**
     * Adds an user to the database.
     * 
     * @param username The username of the user. (it is the attribute that represent an user!).
     * @param attributes The attributes of the user. If some is non-specified, NULL value will be put in the database.
     * 
     * @throws IllegalArgumentException If the username is null, already exists or the set of attributes is null.
     */
    public void addUser(String username, Map<Property,String> attributes) throws IllegalArgumentException{
        if(username == null || attributes == null) {
            IllegalArgumentException e = new IllegalArgumentException("args should not be null");
            logger.error(e.getMessage());
            throw e;
        }
        if(userExists(username)){
            IllegalArgumentException e =  new IllegalArgumentException("User with same superkey already exists");
            logger.error(e.getMessage());
            throw e;
        }
        //first pass: create the SQL
        StringBuilder cmdBuilder = new StringBuilder("INSERT INTO User Values(:username");
        for(User.Property p : User.Property.values()){
            cmdBuilder.append(", :").append(p.toString());
        }
        cmdBuilder.append(");");
        
        
        //second pass: put args in the SQL String
        Connection conn = database.open();
        Query my = conn.createQuery(cmdBuilder.toString());
        
        my.addParameter("username", username);
        for(User.Property p: User.Property.values()){
            //this is a small trick, sql2o handles correctly null values (that is pu a NULL value in tuple).
            String toPut = attributes.getOrDefault(p, null);
            my.addParameter(p.toString(), toPut);
        }
        
        my.executeUpdate();

        conn.close();
    }

    /**
     * Deletes an user in the database
     * 
     * @param username The username of the user. (it is the attribute that represent an user!)
     * 
     * @throws IllegalArgumentException If the specified user does not exist in the database
     */
    public void deleteUser(String username) throws IllegalArgumentException{        
        if(!userExists(username)) {
            IllegalArgumentException e =  new IllegalArgumentException("user does not exists");
            logger.error(e.getMessage());
            throw e;
        }

        String cmd = "DELETE FROM User Where username = :username;";
        Connection conn = database.open();
        
        
        conn.createQuery(cmd).addParameter("username", username).executeUpdate();
        conn.close(); 
    }

    /**
     * Updates the a field of the property of an user.
     * 
     * @param username The username of the user. (it is the attribute that represent an user!).
     * @param p The field to modify.
     * @param v The value to put in place.
     * 
     * @throws IllegalArgumentException If the user does not exists in the database or if the value is null.
     */
    public void updateUserProperty(String username, Property p, String v) throws IllegalArgumentException{
        if(!userExists(username)) {
            IllegalArgumentException e =  new IllegalArgumentException("specified user does not exists in database");
            logger.error(e.getMessage());
            throw e;
        }
        if(v == null) {
            IllegalArgumentException e =  new IllegalArgumentException("args should not be null");
            logger.error(e.getMessage());
            throw e;
        }
        String cmd = "UPDATE User SET "+p.toString().toLowerCase()+"=:v WHERE username=:username;";
        
        Connection conn = database.open();
        conn.createQuery(cmd).addParameter("v", v).addParameter("username", username).executeUpdate();
        conn.close();
    }

    /**
     * This method looks in the database if the user specified by it's username exists.
     * 
     * @param username The username of the user. (it is the attribute that represent an user!)
     * 
     * @return True if the user exists in the library, else false. Also if null value is provided, return false.
     */
    public boolean userExists(String username){
        if(username == null)
            return false;
        
        String cmd = "SELECT S.username FROM User as S where S.username = :username";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).addParameter("username", username).executeAndFetchTable().asList();
        conn.close();
        
        return !boh.isEmpty();
    }

    /**
     * This methods tells wether a node exists or not.
     * 
     * @param username The username of the user.
     * @param nodeId The interger id of the node.
     * 
     * @return true if the node exists, else false.
     */
    public boolean nodeExists(String username, int nodeId){
        if(!userExists(username))
            return false;
        
        String cmd = "SELECT * FROM Node as N WHERE N.username = :username and N.nodeId = :nodeId";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                addParameter("nodeId", nodeId).
                executeAndFetchTable().
                asList();
        conn.close();
        
        return !boh.isEmpty(); 
    }
    
    /**
     * Deletes a node in the database
     * 
     * @param username The username of the user. (it is the attribute that represent an user!)
     * @param nodeId the id of the node
     * 
     * @throws IllegalArgumentException If the node does not exist in the database or the node has children
     */
    public void deleteNode(String username, int nodeId) throws IllegalArgumentException{        
        if(!nodeExists(username, nodeId)) {
            IllegalArgumentException e = new IllegalArgumentException("Node does not exists");
            logger.error(e.getMessage());
            throw e;
        }
        if(childrenFrom(username, nodeId).size() != 0) {
            IllegalArgumentException e =  new IllegalArgumentException("Node has children!");
            logger.error(e.getMessage());
            throw e;
        }
        String cmd = "DELETE FROM Node Where username = :username and nodeid = :nodeId;";
        Connection conn = database.open();
        conn.createQuery(cmd).
            addParameter("username", username).
            addParameter("nodeId", nodeId).
            executeUpdate();
        
        //if no parent, then this query won't do any damage.
        cmd = "DELETE FROM ParentOf Where username = :username and childid = :nodeId;";
        conn.createQuery(cmd).
            addParameter("username", username).
            addParameter("nodeId", nodeId).
            executeUpdate();
        
        conn.close();
    }
    
    /**
     * This method fetches all the property of the user specified by it's username
     * 
     * @param username The username of the user. (it is the attribute that represent an user!)
     * 
     * @return A map of all properties and their associated value for the user. If no value was specified for 
     * a property, then the corresponding value will simply be null. 
     * 
     * @throws IllegalArgumentException If the user does not exist see {@link #userExists(String) userExists}
     */
    public Map<Property, String> fetchUserProperties(String username) throws IllegalArgumentException{        
        if(!userExists(username)) {
            IllegalArgumentException e =  new IllegalArgumentException("The user does not exists");
            logger.error(e.getMessage());
            throw e;
        }
        String cmd = "SELECT * FROM User as S where S.username = :username";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                executeAndFetchTable().asList();
        conn.close();
        
        //now we put the result in a cute map
        HashMap<Property, String> toReturn = new HashMap<>();
        
        for(Property pr: Property.values()){
            //has to be flexible with NULL values
            String value = (boh.get(0).get(pr.toString().toLowerCase()) == null)? 
                    null :
                    boh.get(0).get(pr.toString().toLowerCase()).toString();
            
            toReturn.put(pr, value);
        }

        return toReturn;
    }

    /**
     * Updates the a field of the property of an user.
     * 
     * @param username The username of the user. (it is the attribute that represent an user!).
     * @param nodeId the id of the node
     * @param p The field to modify.
     * @param v The value to put in place.
     * @throws IllegalArgumentException If the user does not exists in the database or if the value is null.
     */
    public void updateNodeProperty(String username, int nodeId, NodeProperties p, String v) throws IllegalArgumentException{
        if(!nodeExists(username, nodeId)){
            IllegalArgumentException e = new IllegalArgumentException("specified node does not exists in database");
            logger.error(e.getMessage());
            throw e;
        }
        if(v == null) {
            IllegalArgumentException e = new IllegalArgumentException("args should not be null");
            logger.error(e.getMessage());
            throw e;
        }
        //note: no need to SQL inject the property name. We trust our own program
        String cmd = "UPDATE Node SET "+p.toString().toLowerCase()+" = :v WHERE username = :username AND nodeid = :nodeId;";
        
        Connection conn = database.open();
        conn.createQuery(cmd).
            addParameter("v", v).
            addParameter("username",  username).
            addParameter("nodeId", nodeId).executeUpdate();
        
        conn.close();
    }

    /**
     * This method creates a GameTree for the player in the database
     * 
     * @param username The unique identifier of the player
     * @param rootId The unique ID of the root (has only to be unique across nodes of the user)
     */
    public void addTree(String username, int rootId){
        if(!nodeExists(username, rootId)) {
            IllegalArgumentException e = new IllegalArgumentException("Node does not exists in the database");
            logger.error(e.getMessage());
            throw e;
        }
        //check that the user has not already a tree
        String cmd = "SELECT * FROM Tree as T where T.username = :username";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                executeAndFetchTable().asList();
        conn.close();
        
        if(!boh.isEmpty()) {
            IllegalArgumentException e = new IllegalArgumentException("The user already has a game tree");
            logger.error(e.getMessage());
            throw e;
        }
        
        //finally execute command
        cmd = "INSERT INTO Tree Values(:username, :rootId)";
        
        conn = database.open();
        conn.createQuery(cmd).addParameter("username", username).addParameter("rootId", rootId).executeUpdate();
        conn.close();        
    }
    
    /**
     * This methods adds a node to the database. The primary keys of the node are it's username (the user to whom
     * the node belongs to), and nodeId the id of the node (note that the nodeId has only to be unique for a particular
     * user.)
     * 
     * @param username The username of the user to whom the node belongs.
     * @param nodeId The id of the node. (has to be unique for the player)
     * @param properties A Map of properties that the node has (if some attributes are non specified, they will be
     * stored as NULL)
     * 
     * @throws IllegalArgumentException If the node already exists or if the user does not exists.
     */
    public void addNode(String username, int nodeId, Map<GameTreeNode.NodeProperties, String> properties){
        if(nodeExists(username, nodeId)) {
            IllegalArgumentException e = new IllegalArgumentException("Node already exists in database");
            logger.error(e.getMessage());
            throw e;
        }
        else if(!userExists(username)){
            IllegalArgumentException e =  new IllegalArgumentException("User does not exists");
            logger.error(e.getMessage());
            throw e;
        }
        else if(properties == null) {
            IllegalArgumentException e =  new IllegalArgumentException("properties cannot be null");
            logger.error(e.getMessage());
            throw e;
        }

        //constructing the string command
        StringBuilder cmdBuilder = new StringBuilder("INSERT INTO Node Values(:username, :nodeId");
        for(GameTreeNode.NodeProperties p : GameTreeNode.NodeProperties.values()){
            cmdBuilder.append(", :").append(p.toString());
        }
        cmdBuilder.append(");");
        
        Connection conn = database.open();
        
        //now we fill the parameters
        Query query = conn.createQuery(cmdBuilder.toString());
        query.addParameter("username", username).addParameter("nodeId", nodeId);        
        for(GameTreeNode.NodeProperties p : GameTreeNode.NodeProperties.values()){
            //this is a small trick, sql2o handles correctly null values (that is pu a NULL value in tuple).
            String toPut = properties.getOrDefault(p, null);
            query.addParameter(p.toString(), toPut);
        }
        
        //finally we can execute the command
        query.executeUpdate();

        conn.close();
    }

    /**
     * This method adds a child node to a node
     * 
     * @param username The unique identifier of the player
     * @param parentId The unique ID of the parent (has only to be unique across nodes of the user)
     * @param childId The unique ID of the child (has only to be unique across nodes of the user)
     * 
     * @throws IllegalArgumentException if the child already has a parent. (In a tree, there is only one parent)
     */
    public void addChild(String username, int parentId, int childId){        
        if(!nodeExists(username, parentId) || !nodeExists(username, childId)) {
            IllegalArgumentException e =  new IllegalArgumentException("parent or child not present in database");
            logger.error(e.getMessage());
            throw e;
        }
        if(parentId == childId) {
            IllegalArgumentException e = new IllegalArgumentException("parent cannot be it's own child (and vice-versa)");
            logger.error(e.getMessage());
            throw e;
        }
        //check that the child has no parent yet
        String cmd = "SELECT * FROM ParentOf as P WHERE P.childId = :childId and P.username = :username";
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("childId", childId).
                addParameter("username", username).
                executeAndFetchTable().asList();
        
        if(!boh.isEmpty()) {
            IllegalArgumentException e = new IllegalArgumentException("this child already has a parent!");
            logger.error(e.getMessage());
            throw e;
        }

        //now we can execute the command
        cmd = "INSERT INTO ParentOf Values(:username, :parentId, :childId)";
        conn.createQuery(cmd).addParameter("username", username).
            addParameter("parentId", parentId).
            addParameter("childId", childId).
                executeUpdate();
        conn.close();
    }

    /**
     * Get the root id of the user's game tree
     * 
     * @param username The unique identifier of the player
     * 
     * @return The id of player game tree's roote.
     */
    public int getRoot(String username){
        if(!userExists(username)) {
            IllegalArgumentException e =  new IllegalArgumentException("Use does not exists in the database");
            logger.error(e.getMessage());
            throw e;
        }

        String cmd = "SELECT rootNodeId FROM Tree as T where T.username = :username";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                executeAndFetchTable().asList();
        conn.close();
                
        if(boh.size() == 0){
            IllegalArgumentException e = new IllegalArgumentException("This user has no gametree yet");
            logger.error(e.getMessage());
            throw e;
        }
  
        return (Integer) boh.get(0).get("rootnodeid");
    }
    
    /**
     * Tells whether the user already has a tree or not
     * 
     * @param username the username that's being checked for hasRoot
     * @return true if user does have tree in database or false elsewise
     */
    public boolean hasRoot(String username){
        String cmd = "SELECT rootNodeId FROM Tree as T where T.username = :username";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                executeAndFetchTable().asList();
        conn.close();
                
        return boh.size() != 0;
    }
    
    /**
     * This method fetch all the properties for the node. If a property was not specified, null is given as value.
     * 
     * @param username The username of the user.
     * @param nodeid The integer identifier.
     * 
     * @return A map containing the value for all properties.
     * 
     * @throws IllegalArgumentException if the node does not exist.
     */
    public Map<GameTreeNode.NodeProperties, String> fetchNodeProperty(String username, int nodeid){

        if(!nodeExists(username, nodeid)) {
            IllegalArgumentException e = new IllegalArgumentException("The user or node does not exists");
            logger.error(e.getMessage());
            throw e;
        }
        
        String cmd = "SELECT * FROM Node as N where N.username = :username and N.nodeid = :nodeId";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                addParameter("nodeId", nodeid).
                executeAndFetchTable().asList();
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
     * @param username The unique identifier of the player
     * @param parentId The unique ID of the parent (has only to be unique across nodes of the user)
     * 
     * @return a list of children id
     */
    public List<Integer> childrenFrom(String username, int parentId){
        if(!nodeExists(username, parentId)){
            IllegalArgumentException e = new IllegalArgumentException("Use does not exists in the database");
            logger.error(e.getMessage());
            throw e;
        }
        
        String cmd = "SELECT childId FROM ParentOf as P where P.username = :username and P.parentId = :parentId";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                addParameter("parentId", parentId).
                executeAndFetchTable().asList();
        conn.close();
        
        ArrayList<Integer> toReturn = new ArrayList<>();
        for(Map<String, Object> m: boh)
            toReturn.add((Integer)m.get("childid"));
        
        return toReturn;
    }

    /**
     * This method finds the parent of a child node.
     * 
     * @param username The unique identifier of the player
     * @param childId The unique ID of the child (has only to be unique across nodes of the user)
     * @return the node from the parent
     */
    public int parentFrom(String username, int childId){
        if(!nodeExists(username, childId)) {
            IllegalArgumentException e = new IllegalArgumentException("node does not exists in the database");
            logger.error(e.getMessage());
            throw e;
        }
        
        String cmd = "SELECT parentId FROM ParentOf as P where P.username = :username and P.childId = :childId";
        
        Connection conn = database.open();
        List<Map<String, Object>> boh = conn.createQuery(cmd).
                addParameter("username", username).
                addParameter("childId", childId).
                executeAndFetchTable().asList();
        conn.close();
        
        if(boh.isEmpty()){
            IllegalArgumentException e = new IllegalArgumentException("this node has no parent! (it's a root node)");
            logger.error(e.getMessage());
            throw e;
        }
        
        return (Integer) boh.get(0).get("parentid");
    }

}
