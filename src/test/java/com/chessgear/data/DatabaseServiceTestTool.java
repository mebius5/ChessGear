package com.chessgear.data;
import com.chessgear.data.DatabaseService;
import com.chessgear.data.FileStorageService;
import com.chessgear.server.User;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.Assert.fail;

/**
 * Since the class is only used for testing purposes, we call faill at any exception thrown. Ideas for reflexivity were found there:
 * 
 * http://stackoverflow.com/questions/5629706/java-accessing-private-constructor-with-type-parameters
 * http://onjavahell.blogspot.com/2009/05/testing-private-methods-using.html
 * 
 * Created by GradyXiao on 12/7/15.
 */
public class DatabaseServiceTestTool {

    /**
     * The values used to fill mock-up database
     */
    public static final String[] usernames = {"gogol", "jean", "hardcorechessplayer"};

    //small trick: evaluation of tests seems to be concurent, so this is to ensure that all test are independents.
    static int number = 0;
    static Object lock = new Object();

    private static void deleteDatabaseIfExists(String prefix){
        //check if the data folder exists
        File general = new File(FileStorageService.DATA_DIRECTORY_NAME);
        if(!general.exists())
            general.mkdir();
        
        File toDel = new File(general.getPath() + File.separator + prefix + DatabaseService.CANONICAL_DB_NAME);
        if(toDel.exists()){
            toDel.delete();

            System.out.println("Deleted " + toDel.getName() + " from previous test run");
        }        
    }
    
    private static void deleteFileFolderIfExists(String suffix){
        //check if the data folder exists
        File general = new File(FileStorageService.DATA_DIRECTORY_NAME);
        if(!general.exists())
            general.mkdir();
        
        File toDel = new File(FileStorageService.DATA_DIRECTORY_NAME + File.separator + 
                FileStorageService.FILE_DIRECTORY_NAME + suffix);
        if(toDel.exists()){
            FileStorageService.deleteRecursively(toDel);
            System.out.println("Deleted " + toDel.getName() + " from previous test run");
        }
    }
    
    /**
     * Creates a new FileStorageService in a fresh folder by mean of reflexivity. Don't forget to destroy it after use.
     * 
     * @return A fresh FileStorageService for tests.
     */
    @SuppressWarnings("unchecked")
    public static FileStorageService createFileStorageService(){        
        //create the prefix to give to the file folder.
        int ticket = 0;
        synchronized(lock){
            ticket = number++;
        }
        String suffix = "erase" + ticket;
        deleteFileFolderIfExists(suffix);

        FileStorageService toReturn = null;
        try {
            Constructor<FileStorageService> constructor = 
                    (Constructor<FileStorageService>) FileStorageService.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true); 
            toReturn = constructor.newInstance(createDatabase(true), suffix);
            constructor.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        return toReturn;
    }

    /**
     * Cleans the workspace of the specified FileStorageService. Also takes care of the referenced DatabaseService
     * 
     * @param fss The FileStorageService to delete.
     */
    public static void destroyFileStorageService(FileStorageService fss){              
        try {
            Method m = fss.getClass().getDeclaredMethod("destroy");
            m.setAccessible(true);
            m.invoke(fss);
            m.setAccessible(false);

            destroyDatabase(fss.getReferecencedDatabaseService());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Creates a fresh DatabaseService for testing puprose, by mean of reflexivity. Don't forget to destroy it after use.
     * 
     * @param fillWithValues specify if the Database has to have some initial mockup value or no.
     * 
     * @return database The actual DatabaseService
     */
    @SuppressWarnings("unchecked")
    public static DatabaseService createDatabase(boolean fillWithValues){
        //create the prefix to give to the file folder.
        int ticket = 0;
        synchronized(lock){
            ticket = number++;
        }
        String prefix = "eraseme" + ticket;
        
        deleteDatabaseIfExists(prefix);

        DatabaseService toReturn = null;

        try {

            Constructor<DatabaseService> constructor = 
                    (Constructor<DatabaseService>) DatabaseService.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true); 
            toReturn = constructor.newInstance(prefix);
            constructor.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        if(fillWithValues){
            for(String address: usernames){
                HashMap<User.Property, String> attributes = new HashMap<User.Property, String>();
                for(User.Property p : User.Property.values()){
                    attributes.put(p, address + p);
                    //since we are going to add properties in the future, just make up something easy
                }
                toReturn.addUser(address, attributes);
            }
        }
        
        return toReturn;
    }

    /**
     * Destroys the database passed in
     * 
     * @param db the database that has to be deleted
     */
    public static void destroyDatabase(DatabaseService db){
        try {
            Method m = db.getClass().getDeclaredMethod("eraseDatabaseFile");
            m.setAccessible(true);
            m.invoke(db);
            m.setAccessible(false);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public static void changeDBinUserClass(DatabaseService replaceWith){
        try{
            Field f = User.class.getDeclaredField("db");
            f.setAccessible(true);
            f.set(null, replaceWith);
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
    
    public static void changeFSSinUserClass(FileStorageService replaceWith){
        try{
            Field f = User.class.getDeclaredField("fss"); //NoSuchFieldException
            f.setAccessible(true);
            f.set(null, replaceWith);
        }
        catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

}
