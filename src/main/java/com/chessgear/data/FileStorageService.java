/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 16, 2015
 */

package com.chessgear.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used to facilitate file storage
 */
public final class FileStorageService {

    /*
     * Note: This is a pretty naïve implementation of a file storage system. To scale better, take a look at amazon S3 services
     */

    public static final String DATA_DIRECTORY_NAME = "data";
    public static final String FILE_DIRECTORY_NAME = "files";

    private final File root;

    private static DatabaseService db;
    private static FileStorageService instance;

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);


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
            if(!general.mkdir()){
                throw new IllegalArgumentException("General directory cannot be made");
            }

        root = new File(DATA_DIRECTORY_NAME + File.separator + FILE_DIRECTORY_NAME + rootDirectorySuffix);
        if(!root.exists())
            if(!root.mkdir()){
                throw new IllegalArgumentException("Root directory cannot be made");
            }
        FileStorageService.db = db;
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
        try {
            if (f.isDirectory()) {
                for (File ff : f.listFiles())
                    deleteRecursively(ff);
            }

            if (!f.delete()) {
                logger.error(f.getName() + " cannot be deleted");
            }
        }catch (NullPointerException e){
            logger.error("Null pointer exception caught during deleteRecursively() for filename: "+f.getName());
        }
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
        try {
            if (!db.userExists(username))
                throw new IllegalArgumentException("User is not in database");

            ArrayList<String> toReturn = new ArrayList<>();

            //check if the user already has a folder
            File userDir = new File(root.getPath() + File.separator + username);
            if (!userDir.exists())
                return toReturn;

            for (File f : userDir.listFiles())
                toReturn.add(f.getName());

            return toReturn;
        } catch(NullPointerException e){
            logger.error("NullPointerException caught during getFilesFor()");
            return null;
        }
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
        if((line=br.readLine())!=null){
            sb.append(line);
        }
        while ((line = br.readLine()) != null) {
            sb.append(System.lineSeparator());
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }

    /**
     * This method removes a file from the server
     * 
     * @param username The name of the user
     * @param fileName The name of the file to store
     * @throws IllegalArgumentException if the user or the file does not exists
     * @throws IOException if there was a problem while removing the file
     */
    public void removeFile(String username, String fileName) throws IllegalArgumentException, IOException{
        if(!db.userExists(username))
            throw new IllegalArgumentException("User is not in database");

        File out = new File(root.getPath() + File.separator + username + File.separator + fileName);
        if(!out.exists())
            throw new IllegalArgumentException("Non existent file");

        if(!out.delete()){
            logger.error(out.getName()+" cannot be deleted");
        }
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
            if(!userDir.mkdir()){
                logger.error("UserDir: "+userDir.getName()+" cannot be created");
            }

        File toStore = new File(userDir.getPath() + File.separator + fileName);
        FileOutputStream fos;
        fos = new FileOutputStream(toStore);

        int read;
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
            if(!userDir.mkdir()){
                logger.error("UserDir: "+userDir.getName()+" cannot be created");
            }

        File toStore = new File(userDir.getPath() + File.separator + fileName);

        PrintWriter stream = new PrintWriter(toStore);
        stream.print(data);
        stream.close();
    }

    /**
     * Reads the contents of an input stream into a String.
     * @param stream Input stream to read.
     * @return Returns a String containing the file information.
     */
    public static String readInputStreamIntoString(InputStream stream) {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(stream);
        if(scanner.hasNextLine()) {
            result.append(scanner.nextLine());
        }
        while (scanner.hasNextLine()) {
            result.append(System.lineSeparator());
            result.append(scanner.nextLine());
        }
        scanner.close();
        return result.toString();
    }

}
