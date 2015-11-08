import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import com.chessgear.data.DatabaseService;
import com.chessgear.server.User.Property;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 8, 2015
 */

public class DatabaseServiceTreeTest {

    static String[] addresses = {"gogol@gmail.com", "jean@jean.fr", "hardcorechessplayer@jhu.edu"};

    //small trick: evaluation of tests seems to be concurent, so this is to ensure that all test are independents.
    static int number = 0;
    Object lock = new Object();
    
    public DatabaseService createDatabase(){
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
            HashMap<Property, String> attributes = new HashMap<Property, String>();
            for(Property p : Property.values()){
                attributes.put(p, address + p); 
                //since we are going to add properties in the future, just make up something easy
            }
            yeh.addUser(address, attributes);
        }
        
        return yeh;
    }
    
    public void destroyDatabase(DatabaseService d){
        try {
            d.eraseDatabaseFile();
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAddTreeActuallyAdds(){
        DatabaseService db = createDatabase();
        db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
        db.addTree("gogol@gmail.com", 1);
        assertEquals(db.getRoot("gogol@gmail.com"), 1);
        destroyDatabase(db);
    }
    
    @Test
    public void testUserHasInitiallyNoTree(){
        DatabaseService db = createDatabase();
        try{
            for(String s : addresses)
                db.getRoot(s);
            
            //if no error was thrown in the meantime, this is a fail
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        }
    }
    
    @Test
    public void testCannotAddTwoRoots(){
        DatabaseService db = createDatabase();
        try{
            db.addNode("jean@jean.fr", 1, Collections.emptyMap());
            db.addTree("jean@jean.fr", 1);
            db.addTree("jean@jean.fr", 1);
            
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
    @Test
    public void testCannotMakeRootFromInexistentNode(){
        DatabaseService db = createDatabase();
        try{
            db.addTree("jean@jean.fr", 78);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
    @Test
    public void testCannotAddTreeToInexistentUser(){
        DatabaseService db = createDatabase();
        try{
            db.addNode("jean@jean.fr", 1, Collections.emptyMap());
            db.addTree("non@existant.user", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }

}
