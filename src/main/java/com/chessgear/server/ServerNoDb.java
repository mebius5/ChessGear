package com.chessgear.server;

import java.util.HashMap;

/**
 * ChessGear server object, with no database integration.
 * Created by Ran on 12/4/2015.
 */
public class ServerNoDb {

    private HashMap<String, UserNoDb> users;

    public ServerNoDb() {
        this.users = new HashMap<>();
    }

    public void addUser(UserNoDb user) {
        this.users.put(user.)
    }
}
