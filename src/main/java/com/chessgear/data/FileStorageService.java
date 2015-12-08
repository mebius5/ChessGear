/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 16, 2015
 */

package com.chessgear.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {

    /*
     * Note: This is a pretty naïve implementation of a file storage system. To scale better, take a look at amazon S3 services
     */

    private final DatabaseService db;
    
    public static final String dataDirectoryName = "data";
    private static final String fileDirectoryName = "files";
    
    private final File root;

    /**
     * Construct a small utility to manage server files.
     * 
     * @param db The app's database (mainly to check user emails)
     * @param rootDirectorySuffix The suffix root of the directory where to put the file (e.g. data) (for testing purpose).
     * 
     * @throws IllegalArgumentException if any of the argument is null
     */
    public FileStorageService(DatabaseService db, String rootDirectorySuffix){
        if(db == null || rootDirectorySuffix == null)
            throw new IllegalArgumentException("Database and root directory cannot be null");

        File general = new File(dataDirectoryName);
        if(!general.exists())
            general.mkdir();
        
        root = new File(dataDirectoryName + File.separator + fileDirectoryName + rootDirectorySuffix);
        if(!root.exists())
            root.mkdir();
   
        this.db = db;
    }
    
    /**
     * For tests only. Also destroy the referenced DatabaseService.
     * 
     * @throws IOException If there was a problem while erasing the database.
     */
    public void destroy() throws IOException{
        deleteRecursively(root);
        db.eraseDatabaseFile();
    }
    
    private void deleteRecursively(File f){
        if(f.isDirectory()){
            for(File ff : f.listFiles())
                deleteRecursively(ff);
        }
        
        f.delete();
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
     * This method removes a file from the server
     * 
     * @param username The name of the user
     * @param fileName The name of the file to store
     * 
     * @throws Exception throws exception if error occurs during removeFile
     * @throws IllegalArgumentException if the user or the file does not exists
     * @throws IOException if there was a problem while removing the file
     */
    public void removeFile(String username, String fileName){
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
}
