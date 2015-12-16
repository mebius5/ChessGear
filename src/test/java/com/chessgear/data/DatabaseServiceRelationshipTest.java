package com.chessgear.data;
import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import com.chessgear.data.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 8, 2015
 */

public class DatabaseServiceRelationshipTest {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceRelationshipTest.class);

    @Test
    public void testAddChildrenReallyAdds(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        db.addNode("gogol", 1, Collections.emptyMap());
        db.addNode("gogol", 2, Collections.emptyMap());
        
        assertEquals(db.childrenFrom("gogol", 1).size(), 0); //has no children
        db.addChild("gogol", 1, 2);
        assertEquals(db.childrenFrom("gogol", 1).size(), 1);
        assertEquals((int) db.childrenFrom("gogol", 1).get(0), 2);
        for(int j = 3; j < 10;  j++){
            db.addNode("gogol", j, Collections.emptyMap());
            db.addChild("gogol", 1, j);
        }
        
        assertNotEquals(db.childrenFrom("gogol", 1).size(), 1); //more stuff was added

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testAddChildrenWithInexistantNodeFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.addChild("gogol", 1, 34);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testAddChildrenWithInexistantNodeFails");
        }
        
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.addChild("gogol", 34, 1);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testAddChildrenWithInexistantNodeFails");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testAddChildrenWithInexistentUserFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.addNode("gogol", 2, Collections.emptyMap()); 
            db.addChild("nonexistentuser", 1, 2);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testAddChildrenWithInexistentUserFails");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testChildCannotBeItsParent(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.addChild("gogol", 1, 1);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testChildCannotBeItsParent");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testChildCannotHaveTwoParent(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addNode("gogol", 1, Collections.emptyMap());
            db.addNode("gogol", 2, Collections.emptyMap()); 
            db.addNode("gogol", 3, Collections.emptyMap()); 
            
            db.addChild("gogol", 1, 3);
            db.addChild("gogol", 2, 3);
            
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testChildCannotHaveTwoParent");
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
        
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        db.addNode("jean", 1, Collections.emptyMap()); 
        db.addNode("jean", 2, Collections.emptyMap()); 
        db.addNode("jean", 3, Collections.emptyMap()); 
        db.addNode("jean", 4, Collections.emptyMap()); 
        db.addNode("jean", 5, Collections.emptyMap()); 
        
        db.addChild("jean", 1, 2);
        db.addChild("jean", 1, 3);
        db.addChild("jean", 3, 4);
        db.addChild("jean", 2, 5);
        
        assertEquals(db.parentFrom("jean", 4), 3);
        assertEquals(db.parentFrom("jean", 5), 2);
        assertEquals(db.parentFrom("jean", 2), 1);
        assertEquals(db.parentFrom("jean", 3), 1);

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testGetParentWhenNoParentThrowsException(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.parentFrom("gogol", 1);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testGetParentWhenNoParentThrowsException");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }  
    }
    
    @Test
    public void testGetParentWhenInexistentNodeFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.parentFrom("gogol", 2);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testGetParentWhenInexistentNodeFails");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }  
   
    }
    
    @Test
    public void testGetParentWhenInexistentUserFails(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        try{
            db.addNode("gogol", 1, Collections.emptyMap()); 
            db.parentFrom("nonexistentuser", 1);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testGetParentWhenInexistentUserFails");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
}
