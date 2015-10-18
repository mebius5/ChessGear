

package com.chessgear.data;

import java.io.IOException;
import java.util.Map;

import com.chessgear.game.Game;
import com.chessgear.server.User;
import static com.chessgear.server.User.Property;

/**
 * @author gilbert
 * 
 * Superkey of an user is it's e-mail adress
 * 
 * This class doesn't build user neither does it handle who is logged or encryption properties.
 * 
 * A class with prewritten querries to the database. Will work on a specified database.
 */
public class DatabaseService {

    /**
     * Construct an easy-to-use representation of the database specified by the file.
     * 
     * @param databasePath The path of the database file
     * 
     * @throws IOException If the database file does not exist. 
     * @throws IllegalArgumentException If the database file does not have the specified tables 
     */
    public DatabaseService(String databasePath) throws IOException, IllegalArgumentException{
        throw new UnsupportedOperationException();
    }
    
    /**
     * Adds an user to the database
     * 
     * @param u The user to add to the database
     * 
     * @throws IllegalArgumentException If user is a null pointer
     */
    public void addUser(User u) throws IllegalArgumentException{
        throw new UnsupportedOperationException();
    }
    
    /**
     * Deletes an user to the database
     * 
     * @param u
     * 
     * @throws IllegalArgumentException If user is a null pointer or user is not present in the database
     */
    public void deleteUser(User u) throws IllegalArgumentException{
        throw new UnsupportedOperationException();   
    }
    
    /**
     * Updates the property of a user 
     * 
     * @param 
     * 
     * @throws IllegalArgumentException If user is a null pointer or user is not present in the database
     */
    public void updateUserProperty(User u, Property p, String v ) throws IllegalArgumentException{
        throw new UnsupportedOperationException(); 
    }

    /**
     * This method looks in the database if the user specified by it's e-mail exists.
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @return True if the user exists in the library, else false.
     * 
     * @throws IllegalArgumentException If string email is null
     */
    public boolean userExists(String email) throws IllegalArgumentException{
        throw new UnsupportedOperationException();
    }
    
    /**
     * This method fetches all the property of the user specified by it's email
     * 
     * @param email The e-mail of the user. (it is the attribute that represent an user!)
     * 
     * @return A map of all properties for the user.
     * 
     * @throws IllegalArgumentException If the user does not exist see {@link #userExists(String) userExists}
     */
    public Map<Property, String> fetchUserProperties(String email) throws IllegalArgumentException{
        if(!userExists(email))
            throw new IllegalArgumentException();
        
        throw new UnsupportedOperationException();
    }
    
    public void addGame(User u, Game g){
        
    }
    
    
    /**
     * This method builds the database. It specify tables, entities, keys and so on.
     * 
     * @param databasePath The path of the database file wanted
     */
    public static void createDatabaseFile(String databasePath){
        
    }
    
    
    
    
}
