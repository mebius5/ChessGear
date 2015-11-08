package com.chessgear;

import com.chessgear.data.DatabaseService;
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

    public void dirty() {
        database = null;
        try {
            database = new DatabaseService("neiltest");
        } catch (IOException | IllegalArgumentException b) {
            System.out.println("Failure to connect to database");
        }
    }

    /**
     * Clearing the Database
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
     * The Main Server
     * @param args this is the main
     */
    public static void main (String[] args) {

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
                System.out.println(corr);
                if (corr.equals(pass)) {
                    response.status(200);
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
                HashMap<User.Property, String> prop = new HashMap<>();
                prop.put(User.Property.PASSWORD, pass);
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
            try {
                int nodeid = Integer.parseInt(request.params(":nodeid"));
            } catch (NumberFormatException e) {
                response.status(404);
                JsonObject error = new JsonObject();
                error.addProperty("why", "not an int");
                return error;
            }

            System.out.println(email);
            return ""; // TODO
        });

        // Handle list retrieval
        get("/chessgear/api/games/list", (request, response) -> {
            return ""; // TODO
        });

        // Handle game import
        put("/chessgear/api/games/import", (request, response) -> {
            return ""; // TODO
        });

    }
    /**
     * Here are the functions that are copies of the put,pull get etc but take a Json Object isntead, for testing.
     */
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

    }
}
