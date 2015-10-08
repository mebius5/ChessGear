package com.chessgear;

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

    public static void main (String[] args) {

        port(PORT);
    }
}
