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

    @Test
    public void testAddChildrenReallyAdds(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
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

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testAddChildrenWithInexistantNodeFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
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
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testAddChildrenWithInexistentUserFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.addNode("gogol@gmail.com", 2, Collections.emptyMap()); 
            db.addChild("non@existent.user", 1, 2);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testChildCannotBeItsParent(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.addChild("gogol@gmail.com", 1, 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testChildCannotHaveTwoParent(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
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
            DatabaseServiceTestTool.destroyDatabase(db);
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
        
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
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

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testGetParentWhenNoParentThrowsException(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.parentFrom("gogol@gmail.com", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }  
    }
    
    @Test
    public void testGetParentWhenInexistentNodeFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.parentFrom("gogol@gmail.com", 2);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }  
   
    }
    
    @Test
    public void testGetParentWhenInexistentUserFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap()); 
            db.parentFrom("non@existent.user", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
}
