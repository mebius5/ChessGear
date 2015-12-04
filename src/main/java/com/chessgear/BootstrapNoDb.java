package com.chessgear;

import com.chessgear.server.ServerNoDb;

import static spark.Spark.*;
/**
 * Alternate bootstrap.
 * Created by Ran on 12/4/2015.
 */
public class BootstrapNoDb {

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";
    private static ServerNoDb server = new ServerNoDb();

    public static void main (String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");





    }


    
}
