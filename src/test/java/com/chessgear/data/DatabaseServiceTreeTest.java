package com.chessgear.data;
import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import com.chessgear.data.DatabaseService;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 8, 2015
 */

public class DatabaseServiceTreeTest {

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
        catch(IllegalArgumentException e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
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
        catch(IllegalArgumentException e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
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
        catch(IllegalArgumentException e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
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
        catch(IllegalArgumentException e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }

}
