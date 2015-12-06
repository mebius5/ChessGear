import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.chessgear.data.DatabaseService;
import com.chessgear.server.User.Property;

import javax.xml.crypto.Data;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 8, 2015
 */

public class DatabaseServiceRelationshipTest {

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
    public void testAddChildrenReallyAdds(){
        DatabaseService db = createDatabase();
        
        db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
        db.addNode("gogol@gmail.com", 2, Collections.emptyMap());
        
        assertEquals(db.childrenFrom("gogol@gmail.com", 1).size(), 0); //has no children
        db.addChild("gogol@gmail.com", 1, 2);
        assertEquals(db.childrenFrom("gogol@gmail.com", 1).size(), 1);
        assertEquals((int) db.childrenFrom("gogol@gmail.com", 1).get(0), 2);
        for(int j = 3; j < 10;  j++){
            db.addNode("gogol@gmail.com", j, Collections.emptyMap());
            db.addChild("gogol@gmail.com", 1, j);
        }
        
        assertNotEquals(db.childrenFrom("gogol@gmail.com", 1).size(), 1); //more stuff was added
          
        destroyDatabase(db);
    }
    
    @Test
    public void testAddChildrenWithInexistantNodeFails(){
        DatabaseService db = createDatabase();
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.addChild("gogol@gmail.com", 1, 34);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.addChild("gogol@gmail.com", 34, 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
    @Test
    public void testAddChildrenWithInexistentUserFails(){
        DatabaseService db = createDatabase();
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.addNode("gogol@gmail.com", 2, Collections.emptyMap()); 
            db.addChild("non@existent.user", 1, 2);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
    @Test
    public void testChildCannotBeItsParent(){
        DatabaseService db = createDatabase();
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.addChild("gogol@gmail.com", 1, 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
    @Test
    public void testChildCannotHaveTwoParent(){
        DatabaseService db = createDatabase();
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
            db.addNode("gogol@gmail.com", 2, Collections.emptyMap()); 
            db.addNode("gogol@gmail.com", 3, Collections.emptyMap()); 
            
            db.addChild("gogol@gmail.com", 1, 3);
            db.addChild("gogol@gmail.com", 2, 3);
            
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
    @Test
    public void testGetParentActuallyReturnsParent(){
        /*
         *         1
         *        / \
         *       /   \
         *      2     3
         *      |     |
         *      5     4
         */
        
        DatabaseService db = createDatabase();
        db.addNode("jean@jean.fr", 1, Collections.emptyMap()); 
        db.addNode("jean@jean.fr", 2, Collections.emptyMap()); 
        db.addNode("jean@jean.fr", 3, Collections.emptyMap()); 
        db.addNode("jean@jean.fr", 4, Collections.emptyMap()); 
        db.addNode("jean@jean.fr", 5, Collections.emptyMap()); 
        
        db.addChild("jean@jean.fr", 1, 2);
        db.addChild("jean@jean.fr", 1, 3);
        db.addChild("jean@jean.fr", 3, 4);
        db.addChild("jean@jean.fr", 2, 5);
        
        assertEquals(db.parentFrom("jean@jean.fr", 4), 3);
        assertEquals(db.parentFrom("jean@jean.fr", 5), 2);
        assertEquals(db.parentFrom("jean@jean.fr", 2), 1);
        assertEquals(db.parentFrom("jean@jean.fr", 3), 1);
        
        destroyDatabase(db);
    }
    
    @Test
    public void testGetParentWhenNoParentThrowsException(){
        DatabaseService db = createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.parentFrom("gogol@gmail.com", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        }  
    }
    
    @Test
    public void testGetParentWhenInexistentNodeFails(){
        DatabaseService db = createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.parentFrom("gogol@gmail.com", 2);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        }  
   
    }
    
    @Test
    public void testGetParentWhenInexistentUserFails(){
        DatabaseService db = createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.parentFrom("non@existent.user", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(db);
        } 
    }
    
}
