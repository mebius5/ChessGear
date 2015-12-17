package com.chessgear.data;

import com.chessgear.server.User.Property;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;


/***
 * Part of Tests for DatabaseService
 */
public class DatabaseServiceUserTest {


    //Logger
    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceUserTest.class);

    public void testAddUserFailsOnNullUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        
        try{
            yeh.addUser(null, Collections.emptyMap());
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testAddUserFailsOnNullUser");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void testUserExists(){
       DatabaseService yeh = DatabaseServiceTestTool.createDatabase(true);
        
        //positive tests
        for(String address: DatabaseServiceTestTool.usernames)
            assertTrue(yeh.userExists(address));
        
        //negative tests
        assertFalse(yeh.userExists("coco"));
        assertFalse(yeh.userExists("badchessplayer"));
        assertFalse(yeh.userExists("mamamia"));

        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    @Test
    public void testUserExistsOnNullValueReturnsFalse(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        assertFalse(yeh.userExists(null));
        logger.info("Ignore previous error message for testUserExistsOnNullValueReturnsFalse");
        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    @Test
    public void testFetchUserProperties(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(true);
        
        for(String address: DatabaseServiceTestTool.usernames){
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
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        try{
            yeh.fetchUserProperties(null);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testFetchUserPropertiesFailsOnNullUser");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void cannotAddExistingAlreadyExistingUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(true);
        
        //this is a small hack to verify that the exception is well thrown and destroy the database anyway. 
        try{
            yeh.addUser("gogol", Collections.emptyMap());
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for cannotAddExistingAlreadyExistingUser");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void nonSpecifyingAPropertyResultsInNullStorage(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        
        yeh.addUser("aaa", Collections.emptyMap());
        assertTrue(yeh.fetchUserProperties("aaa").size() == Property.values().length);

        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    @Test
    public void testUpdateProperty(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(true);
        
        //positives tests
        String ad = "gogol";
        yeh.updateUserProperty(ad, Property.PASSWORD, "gogolito");
        assertEquals(yeh.fetchUserProperties(ad).get(Property.PASSWORD), "gogolito");
        
        ad = "hardcorechessplayer";
        yeh.updateUserProperty(ad, Property.PASSWORD, "somewhatthisismoresecur3");
        assertEquals(yeh.fetchUserProperties(ad).get(Property.PASSWORD), "somewhatthisismoresecur3");
        
        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    public void testUpdatePropertyFailsOnNonexistentKey(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        try{
            yeh.updateUserProperty("nonexistentuser", Property.PASSWORD, "smth");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testUpdatePropertyFailsOnNonexistentKey");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    public void testUpdatePropertyFailsOnNullUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        try{
            yeh.updateUserProperty(null, Property.PASSWORD, "smth");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testUpdatePropertyFailsOnNullUser");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    public void testUpdatePropertyFailsOnNullProperty(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        try{
            yeh.updateUserProperty(null, Property.PASSWORD, "smth");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testUpdatePropertyFailsOnNullProperty");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    @Test
    public void testDeleteUser(){
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(true);
        
        //positive tests
        yeh.deleteUser("gogol");
        assertFalse(yeh.userExists("gogol"));
        
        //negative tests
        assertTrue(yeh.userExists("jean"));
        DatabaseServiceTestTool.destroyDatabase(yeh);
    }
    
    public void testDeleteUserFailsOnNonexistentKey(){
        
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        try{
            yeh.deleteUser("nonexistentuser");
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testDeleteUserFailsOnNonexistentKey");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
    public void testDeleteuserFailsOnNullUser(){
        
        DatabaseService yeh = DatabaseServiceTestTool.createDatabase(false);
        try{
            yeh.deleteUser(null);
            fail();
        }
        catch(Exception e){
            assertEquals(e.getClass(),IllegalArgumentException.class);
            logger.info("Ignore previous error message for testDeleteuserFailsOnNullUser");
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(yeh);
        }
    }
    
}
