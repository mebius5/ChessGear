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
    private DatabaseService database;


    /**
     * Address of server.
     */
    private static final String ADDRESS = "localhost";

    static public DatabaseService dirty() {
        DatabaseService data = null;
        try {
            data = new DatabaseService("neiltest");
        } catch (IOException | IllegalArgumentException b) {
            System.out.println("Failure to connect to database");
        }

        return data;
    }



    public static void main (String[] args) {

        // Initialize server state
        ChessGearServer server = new ChessGearServer();

        port(PORT);
        ipAddress(ADDRESS);
        //neiltest
        final DatabaseService data = dirty();
        HashMap<User.Property, String > map = new HashMap<>();
        //data.addUser("email@email.com", map);
        // Handle login
        post("/chessgear/api/login", (request, response) -> {
            String temp = request.body();
            JsonParser parsed = new JsonParser();
            JsonObject user = parsed.parse(temp).getAsJsonObject();
            String email = user.get("email").getAsString();
            if (data.userExists(email)) {
                String pass = user.get("password").getAsString();
                System.out.println(pass);
                Map<User.Property, String> maps = data.fetchUserProperties(email);
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
            if(!data.userExists(email)) {
                String pass = user.get("password").getAsString();
                HashMap<User.Property, String> prop = new HashMap<>();
                prop.put(User.Property.PASSWORD, pass);
                try {
                    data.addUser(email, prop);
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
        get("/chessgear/api/games/tree", (request, response) -> {
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
}
