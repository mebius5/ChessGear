package com.chessgear;

import com.chessgear.data.*;
import com.chessgear.server.ChessGearServer;
import com.chessgear.server.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.*;

import static spark.Spark.*;

/**
 * ChessGear main class.
 * Created by Ran on 10/8/2015.
 */
public class Bootstrap {
    public static DatabaseService database;

    /**
     * Port for server to listen on.
     */
    private static final int PORT = 8080;

    /**
     * Address of server.
     */
    private static final String ADDRESS = "localhost";

    public static void dirty() {
        database = null;
        try {
            database = new DatabaseService("neil");
            System.out.println("Connected");

        } catch (IOException | IllegalArgumentException b) {
            System.out.println("Failure to connect to database");
            System.exit(0);
        }
    }

    /**
     * Clearing the Database
     */
    public boolean clearDatabase(DatabaseService database) {
        try {
            database.eraseDatabaseFile();
        } catch (IOException e) {
            System.out.println("Failed Deleting database");
            return false;
        }
        return true;
    }
    /**
     * The Main Server
     * @param args this is the main
     */
    public static void main (String[] args) {
        /**
         * The Database for the program
         */
        dirty();

        //initializing the database

        // Initialize server state
        ChessGearServer server = new ChessGearServer();

        port(PORT);
        ipAddress(ADDRESS);
        // Handle login
        post("/chessgear/api/login", (request, response) -> {
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            if (database.userExists(email)) {
                String pass = user.get("password").getAsString();
                System.out.println(pass);
                Map<User.Property, String> maps = database.fetchUserProperties(email);
                String corr = maps.get(User.Property.PASSWORD);
                String username = maps.get(User.Property.USERNAME);
                System.out.println(corr);
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
                JsonObject error = new JsonObject();
                error.addProperty("why", "User Does Not exist");
                response.status(408);
                return error;
            }
            return ""; //TODO
        });

        // Handle register
        put("/chessgear/api/register", (request, response) -> {
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            if (!(database.userExists(email))) {
                String pass = user.get("password").getAsString();
                String username = user.get("username").getAsString();
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

        post("chessgear/api/games/import", "application/json", (request, response) -> {
            System.out.println("Request received for pgn import: " + request.body());
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            User uses = server.getUser(email);
            GameTree tree = uses.getGameTree();
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(request.body());
            JsonObject jsonObject = element.getAsJsonObject();
            String pgn = jsonObject.get("pgn").getAsString();
            PGNParser currentPgnParser = new PGNParser(pgn);
            GameTreeBuilder currentTreeBuilder = new GameTreeBuilder(currentPgnParser.getListOfBoardStates(), currentPgnParser.getWhiteHalfMoves(), currentPgnParser.getBlackHalfMoves());
            tree.addGame(currentTreeBuilder.getListOfNodes());
            return "Success"; // TODO
        });
        get("/chessgear/api/games/tree/:email/:nodeid", (request,response)-> {
            String email = request.params("email");
            int nodeid;
            try {
                nodeid = Integer.parseInt((request.params("nodeid")));
            } catch (NumberFormatException e) {
                response.status(404);
                return errorReturn("Node not found");
            }
            User uses = server.getUser(email);
            if(uses == null) {
                response.status(405);
                return errorReturn("User not logged in");
            }
            GameTree tree = uses.getGameTree();
            GameTreeNode node = tree.getNodeWithId(nodeid);
            if(node == null) {
                response.status(404);
                return errorReturn("Node not found");
            }
            String boardstate = node.getBoardState().toFEN();
            int previous = node.getParent().getId();
            JsonObject ret = new JsonObject();
            ret.addProperty("boardstate", boardstate);
            ret.addProperty("previous", previous);
            return "";
        });
        put("/chessgear/api/logout", (request, response) -> {
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            server.logOutUser(email);
           return "";
        });
        //slightly changed, pass an email instead of username, is now a put request so I can get parameters

        put(" /chessgear/api/:email/property", (request, response) -> {
            String email = request.params("email");

            Map<User.Property, String> maps = database.fetchUserProperties(email);
            if(maps ==null) {
                response.status(405);
                return errorReturn("User does not exist");

            }
            if(server.getUser(email) == null) {
                response.status(405);
               return errorReturn("User is not logged in");
            }
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String prop  = user.get("name").getAsString();
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
            if(server.getUser(email) == null) {
                response.status(405);
                return errorReturn("Not Logged In");
            }
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String prop  = user.get("name").getAsString();
            String value = user.get("value").getAsString();
            if(prop.equals("EMAIL")) {
                if(database.userExists(value)) {
                    response.status(401);
                    return errorReturn("Email Taken");
                } else {
                    Map<User.Property, String> map = database.fetchUserProperties(email);
                    database.addUser(value, map);
                    database.deleteUser(email);
                    response.status(200);
                    return "";
                }
            } else {
                try {
                    database.updateUserProperty(email, User.Property.valueOf(prop), value );
                } catch (IllegalArgumentException e) {
                    response.status(406);
                    return errorReturn("Property Doesn't exist");
                }
            }
            return "";
        });

    }
    public static JsonObject errorReturn(String reason) {
        JsonObject error = new JsonObject();
        error.addProperty("why", reason);
        return error;
    }
}
