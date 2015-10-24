package com.chessgear;

import com.chessgear.server.ChessGearServer;

import static spark.Spark.*;

/**
 * ChessGear main class.
 * Created by Ran on 10/8/2015.
 */
public class Bootstrap {

    /**
     * Port for server to listen on.
     */
    private static final int PORT = 80;

    /**
     * Address of server.
     */
    private static final String ADDRESS = "localhost";

    public static void main (String[] args) {

        // Initialize server state
        ChessGearServer server = new ChessGearServer();

        port(PORT);
        ipAddress(ADDRESS);

        // Handle login
        post("/chessgear/api/login", (request, response) -> {
            return ""; // TODO
        });

        // Handle register
        put("/chessgear/api/register", (request, response) -> {
            return ""; // TODO
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
