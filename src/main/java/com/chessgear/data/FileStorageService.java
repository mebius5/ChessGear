/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 16, 2015
 */

package com.chessgear.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {

    /*
     * Note: This is a pretty naïve implementation of a file storage system. To scale better, take a look at amazon S3 services
     */

    public static final String DATA_DIRECTORY_NAME = "data";
    public static final String FILE_DIRECTORY_NAME = "files";

    private final File root;

    private static DatabaseService db;
    private static FileStorageService instance;

    /**
     * Should be called directly for tests only. Construct a small utility to manage server files.
     * 
     * @param db The app's database (mainly to check user emails)
     * @param rootDirectorySuffix The suffix root of the directory where to put the file (e.g. data) (for testing purpose).
     * 
     * @throws IllegalArgumentException if any of the argument is null
     */
    private FileStorageService(DatabaseService db, String rootDirectorySuffix){
        if(db == null || rootDirectorySuffix == null)
            throw new IllegalArgumentException("Database and root directory cannot be null");

        File general = new File(DATA_DIRECTORY_NAME);
        if(!general.exists())
            general.mkdir();

        root = new File(DATA_DIRECTORY_NAME + File.separator + FILE_DIRECTORY_NAME + rootDirectorySuffix);
        if(!root.exists())
            root.mkdir();

        this.db = db;
    }

    /**
     * Get the singleton instance of the FileStorageService.
     * 
     * @return the unique instance of FileStorageService.
     */
    public static FileStorageService getInstanceOf(){
        if(instance == null)
            instance = new FileStorageService(DatabaseService.getInstanceOf(), "");

        return instance;
    }

    /**
     * For tests only. Does not delete the referenced DatabaseService
     * 
     * @throws IOException If there was a problem while erasing the database.
     */
    @SuppressWarnings("unused")
    private void destroy() throws IOException{
        deleteRecursively(root);
    }

    /**
     * A small utility function that deletes everything in a folder/file recursively
     * 
     * @param f The File (which can be a directory, where the deletion has to start)
     */
    public static void deleteRecursively(File f){
        if(f.isDirectory()){
            for(File ff : f.listFiles())
                deleteRecursively(ff);
        }

        f.delete();
    }

    /**
     * This method gives back the referenced database. (Should only be useful for testing or simplifying calls)
     * 
     * @return the referenced database
     */
    public DatabaseService getReferecencedDatabaseService(){
        return db;
    }

    /**
     * This methods fetches the name of all files that the user has
     * 
     * @param username The name of the user
     * 
     * @return A list of the name of user's files on the server
     * 
     * @throws IllegalArgumentException if the user is not in the database
     */
    public List<String> getFilesFor(String username){
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        ArrayList<String> toReturn = new ArrayList<>();

        //check if the user already has a folder        
        File userDir = new File(root.getPath() + File.separator + username);
        if(!userDir.exists())
            return toReturn;

        for(File f: userDir.listFiles())
            toReturn.add(f.getName());

        return toReturn;
    }

    /**
     * This method is usefull to download a file. It output the FileOutputStream representing the file.
     * 
     * @param username The username of the user
     * @param fileName The name of the file
     * 
     * @return A FileOutputStream representing the file
     * 
     * @throws IllegalArgumentException if the user or the file does not exists
     * @throws IOException if there was a problem while fetching data
     */
    public InputStream downloadFile(String username, String fileName) throws IOException{
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        File out = new File(root.getPath() + File.separator + username + File.separator +fileName);
        if(!out.exists())
            throw new IllegalArgumentException();

        return new FileInputStream(out);
    }

    /**
     * This method is usefull to download a file. It output the FileOutputStream representing the file.
     * 
     * @param username The name of the user
     * @param fileName The name of the file
     * 
     * @return A String representing the content of the file
     * 
     * @throws IllegalArgumentException if the user or the file does not exists
     * @throws IOException If there was a problem while fetching data
     */
    public String fetchFileContent(String username, String fileName) throws IOException{
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        File out = new File(root.getPath() + File.separator + username + File.separator +fileName);
        if(!out.exists())
            throw new IllegalArgumentException();


        BufferedReader br = new BufferedReader(new FileReader(out));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) 
            sb.append(line);
        
        br.close();

        return sb.toString();
    }

    /**
     * This method removes a file from the server
     * 
     * @param username The name of the user
     * @param fileName The name of the file to store
     * 
     * @throws Exception throws exception if error occurs during removeFile
     * @throws IllegalArgumentException if the user or the file does not exists
     * @throws IOException if there was a problem while removing the file
     */
    public void removeFile(String username, String fileName) throws Exception {
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        File out = new File(root.getPath() + File.separator + username + File.separator + fileName);
        if(!out.exists())
            throw new IllegalArgumentException("Non existent file");

        out.delete();
    }

    /**
     * This methods adds a file to the server. It also closes the stream.
     * 
     * @param username The name of the user
     * @param fileName The name of the file
     * @param is The input stream representing the data of the file
     * 
     * @throws IllegalArgumentException if the user does not exists
     * @throws IOException if there was a problem when storing the file
     */
    public void addFile(String username, String fileName, InputStream is) throws IOException{
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        //check if the user already has a folder
        File userDir = new File(root.getPath() + File.separator + username);
        if(!userDir.exists())
            userDir.mkdir();

        File toStore = new File(userDir.getPath() + File.separator + fileName);
        FileOutputStream fos;
        fos = new FileOutputStream(toStore);

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = is.read(bytes)) != -1)
            fos.write(bytes, 0, read);

        fos.close();
        is.close();
    }

    /**
     * This methods adds a file to the server using a String representation for the file.
     * 
     * @param username The name of the user
     * @param fileName The name of the file
     * @param data A String representing the content of the file
     * 
     * @throws IllegalArgumentException if the user does not exists
     * @throws IOException If there was a problem while storing the file.
     */
    public void addFile(String username, String fileName, String data) throws IOException{
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        //check if the user already has a folder
        File userDir = new File(root.getPath() + File.separator + username);
        if(!userDir.exists())
            userDir.mkdir();

        File toStore = new File(userDir.getPath() + File.separator + fileName);

        PrintWriter stream = new PrintWriter(toStore);
        stream.print(data);
        stream.close();
    }

}
