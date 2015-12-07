package com.chessgear;

import com.chessgear.data.*;
import com.chessgear.game.BoardState;
import com.chessgear.game.Game;
import com.chessgear.server.ChessGearServer;
import com.chessgear.server.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static spark.Spark.*;

/**
 * ChessGear main class.
 * Created by Ran on 10/8/2015.
 */
public class Bootstrap {

    DatabaseService db = null;
    private final static FileStorageService fss = null;

    /**
     * Port for server to listen on.
     */
    private static final int PORT = 8080;

    /**
     * The Database for the program
     */
    private static DatabaseService database;

    public Bootstrap() {
        dirty();
    }
    /**
     * Address of server.
     */
    private static final String ADDRESS = "localhost";

    static public void dirty() {
        database = null;
        try {
            database = new DatabaseService("neiltest");
        } catch (IOException | IllegalArgumentException b) {
            System.out.println("Failure to connect to database");
        }
    }

    /**
     * Clearing the Database
     * @return false if error occurs while deleting database. Else, true
     */
    public boolean clearDatabase() {
        try {
            database.eraseDatabaseFile();
        } catch (IOException e) {
            System.out.println("Failed Deleting database");
            return false;
        }
        return true;
    }
    /**
     * The Main Server asdf
     * @param args this is the main
     */
    public static void main (String[] args) {

        // Initialize server state
        ChessGearServer server = new ChessGearServer();
        dirty();
        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");

        //neiltest
        HashMap<User.Property, String > map = new HashMap<>();
        //data.addUser("email@email.com", map);
        // Handle login
        post("/chessgear/api/login", (request, response) -> {
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user;
            try {
                user = parsed.parse(temp).getAsJsonObject();
            } catch (IllegalStateException e) {
                response.status(404);
                return errorReturn("Not Found");
            }
            String email = user.get("email").getAsString();

            System.out.println("Received login request: " + request.body());

            if (database.userExists(email)) {
                String pass = user.get("password").getAsString();
                Map<User.Property, String> maps = database.fetchUserProperties(email);
                String corr = maps.get(User.Property.PASSWORD);
                String username = maps.get(User.Property.USERNAME);
                if (corr.equals(pass)) {
                    response.status(200);
                    User use = new User(username, email, pass);
                    server.addOnlineUser(use, database);
                } else {
                    JsonObject error = new JsonObject();
                    error.addProperty("why", "Incorrect Password");
                    response.status(408);
                    return error;
                }
            } else {
                System.out.println("User does not exist");
                JsonObject error = new JsonObject();
                error.addProperty("why", "User does not exist");
                response.status(408);
                return error;
            }
            return ""; //TODO
        });

        // Handle register
        post("/chessgear/api/register", (request, response) -> {
            System.out.println("User registration request received: " + request.body());
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            if (!(database.userExists(email))) {
                String pass = user.get("password").getAsString();
                String username = user.get("email").getAsString();
                HashMap<User.Property, String> prop = new HashMap<>();
                prop.put(User.Property.PASSWORD, pass);
                prop.put(User.Property.USERNAME, username);
                try {
                    database.addUser(email, prop);
                } catch (IllegalArgumentException e) {
                    response.status(409);
                    JsonObject error = new JsonObject();
                    error.addProperty("why", "Incorrect Format");
                    return error;
                }
                JsonObject ret = new JsonObject();
                ret.addProperty("email", email);
                Map<GameTreeNode.NodeProperties,String> props = new HashMap<>();
                BoardState def = new BoardState();
                def.setToDefaultPosition();
                props.put(GameTreeNode.NodeProperties.BOARDSTATE, def.toFEN());
                props.put(GameTreeNode.NodeProperties.MULTIPLICITY, "1");
                database.addNode(email, 0, props);
                database.addTree(email, 0);
                return ret;
            } else {
                response.status(409);
                JsonObject error = new JsonObject();
                error.addProperty("why", "User already exists");
                return error;
            }
        });

        // Handle tree retrieval
        get("chessgear/api/games/tree/:nodeid", "application/json", (request, response) -> {
            int nodeId = Integer.parseInt(request.params("nodeid"));
            System.out.println("Request received for node " + nodeId);
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            User uses = server.getUser(email);
            GameTree tree = uses.getGameTree();
            if (tree.containsNode(nodeId)) {
                GameTreeNode currentNode = tree.getNodeWithId(nodeId);
                String boardState = currentNode.getBoardState().toFEN();
                List<GameTreeNode> children = currentNode.getChildren();
                GameTreeNode parent = currentNode.getParent();

                List<Integer> childIds = new ArrayList<>();
                for (GameTreeNode currentChildNode : children) {
                    childIds.add(currentChildNode.getId());
                }

                StringBuilder result = new StringBuilder();
                result.append("{ \"boardstate\" : \"");
                result.append(boardState);
                result.append("\", \"children\" : [");
                for (int c = 0; c < childIds.size(); c++) {
                    result.append(childIds.get(c));
                    if (c + 1 != childIds.size()) {
                        result.append(", ");
                    }
                }
                result.append("], \"previousNodeId\" : ");

                if (parent != null) {
                    result.append(parent.getId());
                } else {
                    result.append("null");
                }
                result.append(" }");

                System.out.println(result.toString());
                return result.toString();

            } else {
                response.status(404);
                return "Node does not exist!";
            }
        });
        // Handle list retrieval
        get("/chessgear/api/games/list", (request, response) -> {
            return ""; // TODO
        });
        //Pgn input
        post("chessgear/api/games/import", "application/json", (request, response) -> {
            System.out.println("Request received for pgn import: " + request.body());
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            User uses = server.getUser(email);
            GameTree tree = uses.getGameTree();
            JsonElement element = parsed.parse(request.body());
            JsonObject jsonObject = element.getAsJsonObject();
            String pgn = jsonObject.get("pgn").getAsString();
            PGNParser currentPgnParser = new PGNParser(pgn);
            Game game = new Game(currentPgnParser.getWhitePlayerName(), currentPgnParser.getBlackPlayerName(), null, pgn, currentPgnParser.getResult(), uses.getNumgames());
            uses.addGame(game);
            GameTreeBuilder currentTreeBuilder = new GameTreeBuilder(currentPgnParser.getListOfBoardStates(), currentPgnParser.getWhiteHalfMoves(), currentPgnParser.getBlackHalfMoves());
            tree.addGame(currentTreeBuilder.getListOfNodes());
            return "Success"; // TODO
        });

        get("/chessgear/api/games/tree/:email/:nodeid", (request, response) -> {
            System.out.println("Request received for node " + request.params("nodeid") + " for user " + request.params("email"));
            String email = request.params("email");
            int nodeid;
            try {
                nodeid = Integer.parseInt((request.params("nodeid")));
            } catch (NumberFormatException e) {
                response.status(404);
                return errorReturn("Node not found");
            }
            User uses = server.getUser(email);
            if (uses == null) {
                response.status(405);
                return errorReturn("User not logged in");
            }
            GameTree tree = uses.getGameTree();
            if (tree == null) {
                return errorReturn("Tree doesn't exist!");
            }
            GameTreeNode node = tree.getNodeWithId(nodeid);
            if (node == null) {
                response.status(404);
                return errorReturn("Node not found");
            }
            String boardstate = node.getBoardState().toFEN();
            int previous;
            try {
                previous = node.getParent().getId();
            } catch (NullPointerException e) {
                previous = 0;
            }
            JsonObject ret = new JsonObject();
            ret.addProperty("boardstate", boardstate);
            ret.addProperty("previous", previous);
            return ret;
    });
        //slightly changed, pass an email instead of username, is now a put request so I can get parameters

        put("/chessgear/api/:email/property", (request, response) -> {
            String email = request.params("email");

            Map<User.Property, String> maps = database.fetchUserProperties(email);
            if (maps == null) {
                response.status(405);
                return errorReturn("User does not exist");

            }
            if (server.getUser(email) == null) {
                response.status(405);
                return errorReturn("User is not logged in");
            }
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String prop = user.get("name").getAsString();
            String value;
            try {
                value = maps.get(User.Property.valueOf(prop));
            } catch (IllegalArgumentException e) {
                response.status(406);
                return errorReturn("Bad Property");
            }
            response.status(200);
            JsonObject ret = new JsonObject();
            ret.addProperty("name", value);
            return ret;
        });

        put(" /chessgear/api/:email/property", (request, response) -> {
            String email = request.params("email");
            if (server.getUser(email) == null) {
                response.status(405);
                return errorReturn("Not Logged In");
            }
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String prop = user.get("name").getAsString();
            String value = user.get("value").getAsString();
            if (prop.equals("EMAIL")) {
                if (database.userExists(value)) {
                    response.status(401);
                    return errorReturn("Email Taken");
                } else {
                    Map<User.Property, String> maps = database.fetchUserProperties(email);
                    database.addUser(value, maps);
                    database.deleteUser(email);
                    response.status(200);
                    return "";
                }
            } else {
                try {
                    database.updateUserProperty(email, User.Property.valueOf(prop), value);
                } catch (IllegalArgumentException e) {
                    response.status(406);
                    return errorReturn("Property Doesn't exist");
                }
            }
            return "";
        });
        put("/chessgear/api/logout", (request, response) -> {
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = (parsed.parse(temp)).getAsJsonObject();
            String email = user.get("email").getAsString();
            server.logOutUser(email, database);
            return "";
        });

        /* ---> this is what calls this method
         * <form enctype="multipart/form-data" action="/chessgear/api/games/import/:<useremail>" method="post">
         *   <input id="PGN-FILE" type="file" />
         * </form>
         * 
         * --> we assign ourselves atomic names to the files, to avoid possible conflicts
         */  
        post("/chessgear/api/games/import/:email", (request, response) -> {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            request.raw().setAttribute("org.eclipse.multipartConfig", multipartConfigElement);
            Part file = request.raw().getPart("PGN-FILE"); //file is name of the upload form

            String useremail = request.params(":email");
            if(useremail == null){
                response.status(400);
            }
            else{

                InputStream is = file.getInputStream();
                Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
                String fileAsString = s.hasNext() ? s.next() : "";
                s.close();
                is.close();
                
                //TODO: check that those method indeed add node to the tree
                PGNParser parse = new PGNParser(fileAsString);
                parse.getListOfBoardStates();
               
                try{
                    fss.addFile(useremail, useremail+"@"+System.currentTimeMillis()+".pgn", file.getInputStream());
                    response.status(201);
                }
                catch(IOException e){
                    response.status(400);
                }
                catch(IllegalArgumentException w){
                    response.status(400);
                }
            }
            
            return ""; // TODO
        });

    }
    public static JsonObject errorReturn(String reason) {
        JsonObject error = new JsonObject();
        error.addProperty("why", reason);
        return error;
    }
}
