/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 16, 2015
 */

package com.chessgear.data;

import java.io.File;
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
    private final String rootDirectory;

    /**
     * Construct a small utility to manage server files.
     * 
     * @param db The app's database (mainly to check user emails)
     * @param rootDirectory The root of the directory where to put the file (e.g. data).
     * 
     * @throws IllegalArgumentException if any of the argument is null
     */
    public FileStorageService(DatabaseService db, String rootDirectory){
        if(db == null || rootDirectory == null)
            throw new IllegalArgumentException("Database and root directory cannot be null");

        this.db = db;
        this.rootDirectory = rootDirectory;
    }
    
    
    /**
     * This methods fetches the name of all files that the user has
     * 
     * @param email The user email
     * 
     * @return A list of the name of user's files on the server
     * 
     * @throws IllegalArgumentException if the user is not in the database
     */
    public List<String> getFilesFor(String email){
        if(!db.userExists(email))
            throw new IllegalArgumentException("User is not in database");
        
        
        //check if the general directory already exists
        File dir = new File(rootDirectory);
        if(!dir.exists())
            dir.mkdir();

        //check if the user already has a folder
        File userDir = new File(rootDirectory+"/pgns/"+email);
        if(!userDir.exists())
            dir.mkdir();

        ArrayList<String> toReturn = new ArrayList<>();
        for(File f: userDir.listFiles())
            toReturn.add(f.getName());

        return toReturn;
    }
    
    /**
     * This method is usefull to download a file. It output the FileOutputStream representing the file.
     * 
     * @param email The email of the user
     * @param fileName The name of the file
     * 
     * @return A FileOutputStream representing the file
     * 
     * @throws IllegalArgumentException if the user or de file does not exists
     * @throws IOException if there was a problem while fetching data
     */
    public OutputStream downloadFile(String email, String fileName) throws IOException{
        if(!db.userExists(email))
            throw new IllegalArgumentException("User is not in database");
        
        File out = new File(rootDirectory+"/pgns/"+email+"/"+fileName);
        if(!out.exists())
            throw new IllegalArgumentException();
        
        return new FileOutputStream(out);
    }
    
    /**
     * This method removes a file from the server
     * 
     * @param email The email of the user
     * @param fileName The name of the file to store
     * @throws Exception throws exception if error occurs during removeFile
     * @throws IllegalArgumentException if the user or the file does not exists
     * @throws IOException if there was a problem while removing the file
     */
    public void removeFile(String email, String fileName) throws Exception{
        if(!db.userExists(email))
            throw new IllegalArgumentException("User is not in database");
        
        File out = new File(rootDirectory+"/pgns/"+email+"/"+fileName);
        if(!out.exists())
            throw new IllegalArgumentException();
        
        out.delete();
    }

    /**
     * This methods adds a file to the server
     * 
     * @param email The email of the user
     * @param fileName The name of the file
     * @param is The input stream representing the data of the file
     * 
     * @throws IllegalArgumentException if the user does not exists
     * @throws IOException if there was a problem when storing the file
     */
    public void addFile(String email, String fileName, InputStream is) throws IOException{
        if(!db.userExists(email))
            throw new IllegalArgumentException("User is not in database");

        //check if the general directory already exists
        File dir = new File(rootDirectory);
        if(!dir.exists())
            dir.mkdir();

        //check if the user already has a folder
        File userDir = new File("/pgns/"+email);
        if(!userDir.exists())
            dir.mkdir();

        File toStore = new File(rootDirectory+"/"+fileName);
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
