package com.chessgear;

import static spark.Spark.*;
/**
 * Alternate bootstrap.
 * Created by Ran on 12/4/2015.
 */
public class BootstrapNoDb {

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";

    public static void main (String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");


    }


    
}
