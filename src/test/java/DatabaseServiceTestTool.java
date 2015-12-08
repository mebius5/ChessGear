import com.chessgear.data.DatabaseService;
import com.chessgear.data.FileStorageService;
import com.chessgear.server.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.fail;

/**
 * Created by GradyXiao on 12/7/15.
 */
public class DatabaseServiceTestTool {

    static String[] addresses = {"gogol@gmail.com", "jean@jean.fr", "hardcorechessplayer@jhu.edu"};

    //small trick: evaluation of tests seems to be concurent, so this is to ensure that all test are independents.
    static int number = 0;
    static Object lock = new Object();
        
    /***
     * Deletes db from previous failed test runs, if any
     */
    private static void resetTestEnvironment(){
        try {
            File file1 = new File("."+File.separatorChar);
            File[] files = file1.listFiles();
            for (int i=0;i<file1.listFiles().length;i++){
                if (files[i].getName().contains("erase")) {
                    if(files[i].getCanonicalFile().delete()){
                        System.out.println("Deleted "+files[i].getCanonicalFile().getName()+" from previous test run");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static FileStorageService createFileStorageService(){
        DatabaseService yeeh = createDatabase();
        FileStorageService toReturn;
        
        synchronized(lock){
            toReturn = new FileStorageService(yeeh, "erase"+(number++));
        }
        
        return toReturn;
    }
    
    /***
     * Creates database
     * @return database
     */
    public static DatabaseService createDatabase(){

        resetTestEnvironment();
        DatabaseService yeh = null;
        try {
            synchronized(lock){
                yeh = new DatabaseService("erase"+(number++));
            }
        } catch (IllegalArgumentException e) {
            fail();
            e.printStackTrace();
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }

        for(String address: addresses){
            HashMap<User.Property, String> attributes = new HashMap<User.Property, String>();
            for(User.Property p : User.Property.values()){
                attributes.put(p, address + p);
                //since we are going to add properties in the future, just make up something easy
            }
            yeh.addUser(address, attributes);
        }

        return yeh;
    }

    /***
     * Destroy the database passed in
     * @param d the database that's being deleted
     */
    public static void destroyDatabase(DatabaseService d){
        try {
            d.eraseDatabaseFile();
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
    }
}
