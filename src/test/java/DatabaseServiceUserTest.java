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
        
    public void testAddUserFailsOnNullUser(){
        DatabaseService yeh = createDatabase();
        
        try{
            yeh.addUser(null, Collections.emptyMap());
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
    @Test
    public void testUserExists(){
       DatabaseService yeh = createDatabase();
        
        //positive tests
        for(String address: addresses)
            assertTrue(yeh.userExists(address));
        
        //negative tests
        assertFalse(yeh.userExists("coco@hotmail.com"));
        assertFalse(yeh.userExists("badchessplayer@jhu.edu"));
        assertFalse(yeh.userExists("mamamia@pizza.se"));
        
        destroyDatabase(yeh);
    }
    
    @Test
    public void testUserExistsOnNullValueReturnsFalse(){
        DatabaseService yeh = createDatabase();
        assertFalse(yeh.userExists(null));
        destroyDatabase(yeh);
    }
    
    @Test
    public void testFetchUserProperties(){
        DatabaseService yeh = createDatabase();
        
        for(String address: addresses){
            Map<Property, String> huh = yeh.fetchUserProperties(address);
            
            //the return procedure should hand out each property
            assertTrue(huh.size() == Property.values().length);
            
            //the return procedure should hand out unmodified values
            for(Property p : Property.values()){
                assertEquals(address + p, huh.get(p));
            }
        }
        
        destroyDatabase(yeh);
    }
    
    public void testFetchUserPropertiesFailsOnNullUser(){
        DatabaseService yeh = createDatabase();
        try{
            yeh.fetchUserProperties(null);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
    @Test
    public void cannotAddExistingAlreadyExistingUser(){
        DatabaseService yeh = createDatabase();
        
        //this is a small hack to verify that the exception is well thrown and destroy the database anyway. 
        try{
            yeh.addUser("gogol@gmail.com", Collections.emptyMap());
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
    @Test
    public void nonSpecifyingAPropertyResultsInNullStorage(){
        DatabaseService yeh = createDatabase();
        
        yeh.addUser("aaa@bbb.cc", Collections.emptyMap());
        assertTrue(yeh.fetchUserProperties("aaa@bbb.cc").size() == Property.values().length);
        
        destroyDatabase(yeh);
    }
    
    @Test
    public void testUpdateProperty(){
        DatabaseService yeh = createDatabase();
        
        //positives tests
        String ad = "gogol@gmail.com";
        yeh.updateUserProperty(ad, Property.USERNAME, "gogolito");
        assertEquals(yeh.fetchUserProperties(ad).get(Property.USERNAME), "gogolito");
        
        ad = "hardcorechessplayer@jhu.edu";
        yeh.updateUserProperty(ad, Property.PASSWORD, "somewhatthisismoresecur3");
        assertEquals(yeh.fetchUserProperties(ad).get(Property.PASSWORD), "somewhatthisismoresecur3");
        
        //negative tests
        assertEquals(yeh.fetchUserProperties(ad).get(Property.USERNAME), ad + Property.USERNAME);
        
        destroyDatabase(yeh);
    }
    
    public void testUpdatePropertyFailsOnNonexistentKey(){
        DatabaseService yeh = createDatabase();
        try{
            yeh.updateUserProperty("non@existent.user", Property.PASSWORD, "smth");
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);   
        }
    }
    
    public void testUpdatePropertyFailsOnNullUser(){
        DatabaseService yeh = createDatabase();
        try{
            yeh.updateUserProperty(null, Property.PASSWORD, "smth");
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
    public void testUpdatePropertyFailsOnNullProperty(){
        DatabaseService yeh = createDatabase();
        try{
            yeh.updateUserProperty(null, Property.PASSWORD, "smth");
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
    @Test
    public void testDeleteUser(){
        DatabaseService yeh = createDatabase();
        
        //positive tests
        yeh.deleteUser("gogol@gmail.com");
        assertFalse(yeh.userExists("gogol@gmail.com"));
        
        //negative tests
        assertTrue(yeh.userExists("jean@jean.fr"));
        destroyDatabase(yeh);
    }
    
    public void testDeleteUserFailsOnNonexistentKey(){
        
        DatabaseService yeh = createDatabase();
        try{
            yeh.deleteUser("non@existent.user");
            fail();
        }
        catch(IllegalArgumentException e){
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
    public void testDeleteuserFailsOnNullUser(){
        
        
        DatabaseService yeh = createDatabase();
        try{
            yeh.deleteUser(null);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            destroyDatabase(yeh);
        }
    }
    
}
