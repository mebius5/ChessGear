import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.chessgear.data.DatabaseService;
import com.chessgear.server.User.Property;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Oct 24, 2015
 */

/***
 * Test for DatabaseServer User
 */
public class DatabaseServiceUserTest {
        
    public void testAddUserFailsOnNullUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        try{
            yeh.addUser(null, Collections.emptyMap());
            fail();
        }
        catch(IllegalArgumentException e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void testUserExists(){
       DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        //positive tests
        for(String address: DatabaseServiceTestTool.addresses)
            assertTrue(yeh.userExists(address));
        
        //negative tests
        assertFalse(yeh.userExists("coco@hotmail.com"));
        assertFalse(yeh.userExists("badchessplayer@jhu.edu"));
        assertFalse(yeh.userExists("mamamia@pizza.se"));

        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    @Test
    public void testUserExistsOnNullValueReturnsFalse(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        assertFalse(yeh.userExists(null));
        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    @Test
    public void testFetchUserProperties(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        for(String address: DatabaseServiceTestTool.addresses){
            Map<Property, String> huh = yeh.fetchUserProperties(address);
            
            //the return procedure should hand out each property
            assertTrue(huh.size() == Property.values().length);
            
            //the return procedure should hand out unmodified values
            for(Property p : Property.values()){
                assertEquals(address + p, huh.get(p));
            }
        }

        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    public void testFetchUserPropertiesFailsOnNullUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        try{
            yeh.fetchUserProperties(null);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void cannotAddExistingAlreadyExistingUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        //this is a small hack to verify that the exception is well thrown and destroy the database anyway. 
        try{
            yeh.addUser("gogol@gmail.com", Collections.emptyMap());
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void nonSpecifyingAPropertyResultsInNullStorage(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        yeh.addUser("aaa@bbb.cc", Collections.emptyMap());
        assertTrue(yeh.fetchUserProperties("aaa@bbb.cc").size() == Property.values().length);

        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    @Test
    public void testUpdateProperty(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        //positives tests
        String ad = "gogol@gmail.com";
        yeh.updateUserProperty(ad, Property.EMAIL, "gogolito");
        assertEquals(yeh.fetchUserProperties(ad).get(Property.EMAIL), "gogolito");
        
        ad = "hardcorechessplayer@jhu.edu";
        yeh.updateUserProperty(ad, Property.PASSWORD, "somewhatthisismoresecur3");
        assertEquals(yeh.fetchUserProperties(ad).get(Property.PASSWORD), "somewhatthisismoresecur3");
        
        //negative tests
        assertEquals(yeh.fetchUserProperties(ad).get(Property.EMAIL), ad + Property.EMAIL);

        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    public void testUpdatePropertyFailsOnNonexistentKey(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        try{
            yeh.updateUserProperty("non@existent.user", Property.PASSWORD, "smth");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);

        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    public void testUpdatePropertyFailsOnNullUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        try{
            yeh.updateUserProperty(null, Property.PASSWORD, "smth");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);

        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    public void testUpdatePropertyFailsOnNullProperty(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        try{
            yeh.updateUserProperty(null, Property.PASSWORD, "smth");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void testDeleteUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        
        //positive tests
        yeh.deleteUser("gogol@gmail.com");
        assertFalse(yeh.userExists("gogol@gmail.com"));
        
        //negative tests
        assertTrue(yeh.userExists("jean@jean.fr"));
        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    public void testDeleteUserFailsOnNonexistentKey(){
        
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        try{
            yeh.deleteUser("non@existent.user");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    public void testDeleteuserFailsOnNullUser(){
        
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase();
        try{
            yeh.deleteUser(null);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
}
