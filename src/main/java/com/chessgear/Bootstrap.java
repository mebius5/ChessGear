package com.chessgear;

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
    private static final int PORT = 80;

    /**
     * Address of server.
     */
    private static final String ADDRESS = "localhost";

    public static void main (String[] args) {

        // Initialize server state

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
        /* ---> this is what calls this method
         * <form enctype="multipart/form-data" action="/chessgear/api/games/import/:<useremail>" method="post">
         *   <input id="PGN-FILE" type="file" />
         * </form>
         * 
         * --> we assign ourselves atomic names to the files, to avoid possible conflicts
         */     
        post("/chessgear/api/games/import/:user", (request, response) -> {
            // code taken here: http://deniz.dizman.org/file-uploads-using-spark-java-micro-framework/: to refactor

            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            request.raw().setAttribute("org.eclipse.multipartConfig", multipartConfigElement);
            Part file = request.raw().getPart("PGN-FILE"); //file is name of the upload form

            String useremail = request.params(":user");
            if(useremail == null){
                response.status(400);
            }
            else{

                InputStream is = file.getInputStream();
                Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
                String fileAsString = s.hasNext() ? s.next() : "";
                s.close();
                is.close();
                
                //do something with the fileAsString

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
}
