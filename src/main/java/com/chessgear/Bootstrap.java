package com.chessgear;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.PGNParser;
import com.chessgear.server.ChessGearServer;
import com.chessgear.server.User;
import com.google.gson.Gson;
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
        //dirty();

        //initializing the database

        // Initialize server state
        ChessGearServer server = new ChessGearServer();

        port(PORT);
        ipAddress(ADDRESS);
        //neiltest
        HashMap<User.Property, String > map = new HashMap<>();
        //data.addUser("email@email.com", map);
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
            if(!database.userExists(email)) {
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
                return ret;
            } else {
                response.status(409);
                JsonObject error = new JsonObject();
                error.addProperty("why", "User already exists");
                return error;
            }
        });

        // Handle tree retrieval
        get("/chessgear/api/games/tree/:email/:nodeid", (request, response) -> {
            String email = request.params(":email");
            int nodeid = 0;
            try {
                nodeid = Integer.parseInt(request.params(":nodeid"));
            } catch (NumberFormatException e) {
                response.status(404);
                JsonObject error = new JsonObject();
                error.addProperty("why", "not an int");
                return error;
            }

            return ""; // TODO
        });

        // Handle list retrieval
        get("/chessgear/api/games/list", (request, response) -> {
            return ""; // TODO
        });

        // Handle game import
        put(" /chessgear/api/games/import/:email", (request, response) -> {
            String email = request.params(":email");
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String PGN = user.get("pgn").getAsString();
            PGNParser parse = new PGNParser(PGN);
            parse.getListOfBoardStates();
            return ""; // TODO
        });

    }
    /**
     * Here are the functions that are copies of the put,pull get etc but take a Json Object isntead, for testing.

    public JsonObject createUser(JsonObject request) {
        int status;
        String temp = request.toString();
        JsonParser parsed = new JsonParser();
        JsonObject user = parsed.parse(temp).getAsJsonObject();
        String email = user.get("email").getAsString();
        if(!database.userExists(email)) {
            String pass = user.get("password").getAsString();
            HashMap<User.Property, String> prop = new HashMap<>();
            prop.put(User.Property.PASSWORD, pass);
            try {
                database.addUser(email, prop);
            } catch (IllegalArgumentException e) {
                status = 409;
                JsonObject error = new JsonObject();
                error.addProperty("why", "Incorrect Format");
                error.addProperty("status", status);
                return error;
            }
            JsonObject ret = new JsonObject();
            ret.addProperty("email", email);
            return ret;
        } else {
            status = 409;
            JsonObject error = new JsonObject();
            error.addProperty("why", "User already exists");
            error.addProperty("status", status);
            return error;
        }

    }*/
}
