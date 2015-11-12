package com.chessgear;

import static spark.Spark.*;

/**
 * Created by Ran on 11/12/2015.
 */
public class DemoBootStrap {

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";

    public static void main(String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");

        get("chessgear/api/games/tree/:nodeid", (request, response) -> {
            int nodeId = Integer.parseInt(request.params("nodeid"));
            return "Request for node # " + nodeId;
        });

    }
}
