package com.chessgear;

import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeBuilder;
import com.chessgear.data.GameTreeNode;
import com.chessgear.data.PGNParser;
import com.chessgear.server.ServerNoDb;
import com.chessgear.server.UserNoDb;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static spark.Spark.*;
/**
 * Alternate bootstrap.
 * Created by Ran on 12/4/2015.
 */
public class BootstrapNoDb {

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";
    private static ServerNoDb server = new ServerNoDb();
    private static JsonParser parser = new JsonParser();

    public static void main (String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");

        post("/chessgear/api/register", "application/json", (request, response) -> {
            System.out.println("User registration request receieved: " + request.body());
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String user = parsedRequest.get("user").getAsString().toLowerCase();
            String password = parsedRequest.get("password").getAsString();

            // Check if user already exists. If so, we can't do this registration.
            if (server.userExists(user)) {
                System.out.println("Failure : user already exists!");
                response.status(409);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User already exists!");
                return failureResponse;
            } else {
                System.out.println("User " + user + " created!");
                server.addUser(new UserNoDb(user, password));
                response.status(200);
                JsonObject successResponse = new JsonObject();
                successResponse.addProperty("user", user);
                return successResponse;
            }
        });

        post("/chessgear/api/login", "application/json", (request, response) -> {
            System.out.println("User login request received: " + request.body());
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String user = parsedRequest.get("user").getAsString().toLowerCase();
            String password = parsedRequest.get("password").getAsString();

            // Check if user exists. If not, login fails.
            if (server.userExists(user)) {
                UserNoDb serverUser = server.getUser(user);
                if (serverUser.getPassword().equals(password)) {
                    System.out.println("Login successful");
                    response.status(200);
                    JsonObject successResponse = new JsonObject();
                    successResponse.addProperty("user", user);
                    return successResponse;
                } else {
                    System.out.println("Password incorrect");
                    response.status(408);
                    JsonObject failureResponse = new JsonObject();
                    failureResponse.addProperty("why", "Password incorrect!");
                    return failureResponse;
                }

            } else {
                System.out.println("User does not exist!");
                response.status(408);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }
        });


        post("/chessgear/api/games/import/:username", "application/json", (request, response) -> {
            System.out.println("Game import request received" + request.body());
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String pgn = parsedRequest.get("pgn").getAsString();
            String user = request.params("username").toLowerCase();

            // Check if user exists. If so, fetch its tree, and add the game.
            if (server.userExists(user)) {
                // Also add the game to the user's list of games.
                UserNoDb currentUser = server.getUser(user);
                currentUser.addGame(pgn);
                GameTree currentTree = currentUser.getGameTree();
                GameTreeBuilder treeBuilder = new GameTreeBuilder(new PGNParser(pgn));
                currentTree.addGame(treeBuilder.getListOfNodes());
                // Return response
                response.status(201);
                JsonObject successResponse = new JsonObject();
                successResponse.addProperty("user", user);
                System.out.println("Game import success!");
                return successResponse;
            } else {
                System.out.println("Import failed: user does not exist!");
                response.status(400);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }

        });

        get("/chessgear/api/games/tree/:username/:nodeid", "application/json", (request, response) -> {
            String user = request.params("username").toLowerCase();
            int nodeId = Integer.parseInt(request.params("nodeid"));
            System.out.println("Node request recieved for user " + user + ", node " + nodeId);

            if (server.userExists(user)) {
                GameTree currentTree = server.getUser(user).getGameTree();
                if (currentTree.containsNode(nodeId)) {
                    GameTreeNode currentNode = currentTree.getNodeWithId(nodeId);
                    response.status(200);
                    System.out.println(currentNode.getJson());
                    return currentNode.getJson();
                } else {
                    System.out.println("Request failed: node not found!");
                    response.status(404);
                    JsonObject failureResponse = new JsonObject();
                    failureResponse.addProperty("why", "Node not found!");
                    return failureResponse;
                }



            } else {
                System.out.println("Request failed: user does not exist!");
                response.status(405);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }
        });


    }


    
}
