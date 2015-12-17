package com.chessgear.data;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/***
 * Part of Tests for DatabaseService.java
 */

public class DatabaseServiceTreeTest {

    //Logger
    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceTreeTest.class);


    @Test
    public void testAddTreeActuallyAdds(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        db.addNode("gogol", 1, Collections.emptyMap());
        db.addTree("gogol", 1);
        assertEquals(db.getRoot("gogol"), 1);
        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testUserHasInitiallyNoTree(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            for(String s : DatabaseServiceTestTool.usernames)
                db.getRoot(s);
            
            //if no error was thrown in the meantime, this is a fail
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testUserHasInitiallyNoTree");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }
    }
    
    @Test
    public void testCannotAddTwoRoots(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addNode("jean", 1, Collections.emptyMap());
            db.addTree("jean", 1);
            db.addTree("jean", 1);
            
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testCannotAddTwoRoots");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testCannotMakeRootFromInexistentNode(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addTree("jean", 78);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testCannotMakeRootFromInexistentNode");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testCannotAddTreeToInexistentUser(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        try{
            db.addNode("jean", 1, Collections.emptyMap());
            db.addTree("nonexistantuser", 1);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testCannotAddTreeToInexistentUser");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }

}
