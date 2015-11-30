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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Scanner;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.FileStorageService;

/**
 * ChessGear main class.
 * Created by Ran on 10/8/2015.
 */
public class Bootstrap {

    DatabaseService db = null;
    private final static FileStorageService fss = null;


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


        /* ---> this is what calls this method
         * <form enctype="multipart/form-data" action="/chessgear/api/games/import/:<useremail>" method="post">
         *   <input id="PGN-FILE" type="file" />
         * </form>
         * 
         * --> we assign ourselves atomic names to the files, to avoid possible conflicts
         */  
        post("/chessgear/api/games/import/:email", (request, response) -> {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            request.raw().setAttribute("org.eclipse.multipartConfig", multipartConfigElement);
            Part file = request.raw().getPart("PGN-FILE"); //file is name of the upload form

            String useremail = request.params(":email");
            if(useremail == null){
                response.status(400);
            }
            else{

                InputStream is = file.getInputStream();
                Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
                String fileAsString = s.hasNext() ? s.next() : "";
                s.close();
                is.close();
                
                //TODO: check that those method indeed add node to the tree
                PGNParser parse = new PGNParser(fileAsString);
                parse.getListOfBoardStates();
               
                try{
                    fss.addFile(useremail, useremail+"@"+System.currentTimeMillis()+".pgn", file.getInputStream());
                    response.status(201);
                }
                catch(IOException e){
                    response.status(400);
                }
                catch(IllegalArgumentException w){
                    response.status(400);
                }
            }
            
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
