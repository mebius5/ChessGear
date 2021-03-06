package com.chessgear;

import com.chessgear.data.FileStorageService;
import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeNode;
import com.chessgear.data.PGNParseException;
import com.chessgear.server.Server;
import com.chessgear.server.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import static spark.Spark.*;
/**
 * Main class. Handles REST requests.
 * Created by Ran on 12/4/2015.
 */
public final class Bootstrap {

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";
    private static Server server = new Server();
    private static JsonParser parser = new JsonParser();
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main (String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");

        // Handle registration request.
        post("/chessgear/api/register", "application/json", (request, response) -> {
            logger.info("User registration request receieved: " + request.body());
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String user = parsedRequest.get("user").getAsString().toLowerCase();
            String password = parsedRequest.get("password").getAsString();

            // Check if user already exists. If so, we can't do this registration.
            if (server.userExists(user)) {
                logger.warn("Failure : user already exists!");
                response.status(409);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User already exists!");
                return failureResponse;
            } else {
                logger.error("User " + user + " created!");
                server.addUser(User.registerNewUser(user, password));
                response.status(200);
                JsonObject successResponse = new JsonObject();
                successResponse.addProperty("user", user);
                return successResponse;
            }
        });

        // Handle login.
        post("/chessgear/api/login", "application/json", (request, response) -> {
            logger.info("User login request received: " + request.body());
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String user = parsedRequest.get("user").getAsString().toLowerCase();
            String password = parsedRequest.get("password").getAsString();

            // Check if user exists. If not, login fails.
            if (server.userExists(user)) {
                User serverUser = server.getUser(user);
                if (serverUser.getPassword().equals(password)) {
                    logger.info("Login successful");
                    response.status(200);
                    JsonObject successResponse = new JsonObject();
                    successResponse.addProperty("user", user);
                    return successResponse;
                } else {
                    logger.warn("Password incorrect");
                    response.status(408);
                    JsonObject failureResponse = new JsonObject();
                    failureResponse.addProperty("why", "Password incorrect!");
                    return failureResponse;
                }

            } else {
                logger.error("User does not exist!");
                response.status(408);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }
        });

        // Handle file import
        post("/chessgear/api/games/importfile/:username", "multipart/form-data", (request, response) -> {
            String user = request.params("username").toLowerCase();
            logger.info("Receiving file import request for user " + user);

            if (server.userExists(user)) {
                // Also add the game to the user's list of games.
                User currentUser = server.getUser(user);

                // Read in file contents.
                MultipartConfigElement configElement = new MultipartConfigElement("/tmp");
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", configElement);
                Part file = request.raw().getPart("file");
                String pgn = FileStorageService.readInputStreamIntoString(file.getInputStream());

                currentUser.addGame(pgn);
                // Return response
                response.status(201);
                JsonObject successResponse = new JsonObject();
                successResponse.addProperty("user", user);
                logger.info("Game import success!");
                return successResponse;
            } else {
                logger.error("Import failed: user does not exist!");
                response.status(400);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }
        });

        // Handle import.
        post("/chessgear/api/games/import/:username", "application/json", (request, response) -> {
            logger.info("Game import request received" + request.body());
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String pgn = parsedRequest.get("pgn").getAsString();
            String user = request.params("username").toLowerCase();

            // Check if user exists. If so, fetch its tree, and add the game.
            if (server.userExists(user)) {
                // Also add the game to the user's list of games.
                User currentUser = server.getUser(user);
                try {
                    currentUser.addGame(pgn);
                } catch (PGNParseException e) {
                    response.status(400);
                    JsonObject failureResponse = new JsonObject();
                    failureResponse.addProperty("why", "Failed to parse PGN: " + e.getMessage());
                    return failureResponse;
                }
                // Return response
                response.status(201);
                JsonObject successResponse = new JsonObject();
                successResponse.addProperty("user", user);
                logger.info("Game import success!");
                return successResponse;
            } else {
                logger.error("Import failed: user does not exist!");
                response.status(400);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }

        });

        // Handle node request
        get("/chessgear/api/games/tree/:username/:nodeid", "application/json", (request, response) -> {
            String user = request.params("username").toLowerCase();
            int nodeId = Integer.parseInt(request.params("nodeid"));
            logger.info("Node request received for user " + user + ", node " + nodeId);

            if (server.userExists(user)) {
                GameTree currentTree = server.getUser(user).getGameTree();
                if (currentTree.containsNode(nodeId)) {
                    GameTreeNode currentNode = currentTree.getNodeWithId(nodeId);
                    response.status(200);
                    logger.info(currentNode.getJson());
                    return currentNode.getJson();
                } else {
                    logger.error("Request failed: node not found!");
                    response.status(404);
                    JsonObject failureResponse = new JsonObject();
                    failureResponse.addProperty("why", "Node not found!");
                    return failureResponse;
                }
            } else {
                logger.error("Request failed: user does not exist!");
                response.status(405);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User does not exist!");
                return failureResponse;
            }
        });

        // Handle games list request.
        get("/chessgear/api/games/list/:username", "application/json", (request, response) -> {
            String user = request.params("username").toLowerCase();
            logger.info("Games list request received for user " + user);
            if (server.userExists(user)) {

                response.status(200);
                return server.getUser(user).getGamesJson();

            } else {
                logger.error("Request failed: user not found!");
                response.status(405);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User not found!");
                return failureResponse;
            }
        });

        // Handle game pgn request.
        get("/chessgear/api/games/:username/:gameId", "application/json", (request, response) -> {
            String user = request.params("username").toLowerCase();
            int gameId = Integer.parseInt(request.params("gameId"));
            logger.info("Game pgn request received for " + user + ", game " + gameId);
            if (server.userExists(user)) {
                if (server.getUser(user).getGameById(gameId) != null) {
                    response.status(200);
                    response.type("text/plain");
                    return server.getUser(user).getGameById(gameId).getPgn();
                } else {
                    logger.error("Request failed: game not found");
                    JsonObject failureResponse = new JsonObject();
                    failureResponse.addProperty("why", "Game not found!");
                    return failureResponse;
                }
            } else {
                logger.error("Request failed: user not found");
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User not found!");
                return failureResponse;
            }
        });

        // Update user password
        post("/chessgear/api/account", "application/json", (request, response) -> {
            JsonObject parsedRequest = parser.parse(request.body()).getAsJsonObject();
            String user = parsedRequest.get("user").getAsString().toLowerCase();
            String password = parsedRequest.get("password").getAsString();
            logger.info("Password change request for " + user + " received");

            if (server.userExists(user)) {
                User currentUser = server.getUser(user);
                currentUser.setPassword(password);
                response.status(200);
                return "";
            } else {
                logger.error("Request failed: user not found!");
                response.status(405);
                JsonObject failureResponse = new JsonObject();
                failureResponse.addProperty("why", "User not found!");
                return failureResponse;
            }
        });
    }
}
