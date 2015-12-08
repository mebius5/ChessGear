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

    @Test
    public void testAddTreeActuallyAdds(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
        db.addTree("gogol@gmail.com", 1);
        assertEquals(db.getRoot("gogol@gmail.com"), 1);
        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testUserHasInitiallyNoTree(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
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
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        try{
            db.addNode("jean@jean.fr", 1, Collections.emptyMap());
            db.addTree("jean@jean.fr", 1);
            db.addTree("jean@jean.fr", 1);
            
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
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        try{
            db.addTree("jean@jean.fr", 78);
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
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        try{
            db.addNode("jean@jean.fr", 1, Collections.emptyMap());
            db.addTree("non@existant.user", 1);
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
