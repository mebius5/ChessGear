import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.GameTreeNode;
import com.chessgear.data.GameTreeNode.NodeProperties;
import com.chessgear.server.User.Property;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 8, 2015
 */

public class DatabaseServiceNodeTest {

    @Test
    public void testAddNodeReallyAdds(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        //examples with no properties
        db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
        assertTrue(db.nodeExists("gogol@gmail.com", 1));
        assertFalse(db.nodeExists("gogol@gmail.com", 2));
        db.addNode("gogol@gmail.com", 2, Collections.emptyMap());
        assertTrue(db.nodeExists("gogol@gmail.com", 2));
        
        //example with properties
        assertFalse(db.nodeExists("jean@jean.fr", 1));
        Map<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        for(GameTreeNode.NodeProperties p : GameTreeNode.NodeProperties.values())
            prop.put(p, p+"gogol");
        db.addNode("jean@jean.fr", 1, prop);
        assertTrue(db.nodeExists("jean@jean.fr", 1));

        DatabaseServiceTestTool.destroyDatabase(db);
    }

    @Test
    public void testCannotAddTwiceANode(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
            db.addNode("gogol@gmail.com", 1, Collections.emptyMap());
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }
    }

    @Test
    public void testCannotAddNodeWithInexistentUser(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.addNode("inexistant@user.com", 1, Collections.emptyMap());
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testFetchPropertiesNodeWorks(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        HashMap<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        prop.put(NodeProperties.EVAL, "0.78");
        
        db.addNode("gogol@gmail.com", 1, prop);
        assertEquals(db.fetchNodeProperty("gogol@gmail.com", 1).get(NodeProperties.EVAL), "0.78");
        
        //a second example with null value
        db.addNode("gogol@gmail.com", 2, Collections.emptyMap());
        assertTrue(db.fetchNodeProperty("gogol@gmail.com", 2).get(NodeProperties.EVAL) == null);

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testUpdatePropertyWorks(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        HashMap<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        prop.put(NodeProperties.EVAL, "0.78");
        
        db.addNode("gogol@gmail.com", 1, prop);
        assertEquals(db.fetchNodeProperty("gogol@gmail.com", 1).get(NodeProperties.EVAL), "0.78");
        
        db.updateNodeProperty("gogol@gmail.com", 1, GameTreeNode.NodeProperties.EVAL, "0.333");
        assertEquals(db.fetchNodeProperty("gogol@gmail.com", 1).get(NodeProperties.EVAL), "0.333");

        DatabaseServiceTestTool.destroyDatabase(db);
        
    }
    
    @Test
    public void testUpdatePropertyThrowsException(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.updateNodeProperty("inexistant@user.com", 1, GameTreeNode.NodeProperties.EVAL, "0.333");
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testRemoveWorks(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        HashMap<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        prop.put(NodeProperties.EVAL, "0.78");
        db.addNode("gogol@gmail.com", 1, prop);
        
        assertTrue(db.nodeExists("gogol@gmail.com", 1));
        
        db.deleteNode("gogol@gmail.com", 1);
        
        assertFalse(db.nodeExists("gogol@gmail.com", 1));

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testRemoveThrowsException(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase();
        
        try{
            db.deleteNode("inexistant@user.com", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        } 
    }
    
    @Test
    public void testRemoveWithChildrenThrowsException(){
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
        
        // a positive test
        db.deleteNode("jean@jean.fr", 5);
        db.deleteNode("jean@jean.fr", 2);
       
        //this one should fail!
        try{
            db.deleteNode("jean@jean.fr", 3);
            fail();
        }
        catch(IllegalArgumentException e){
            
        }
        finally{
            DatabaseServiceTestTool.destroyDatabase(db);
        }
    }
    
}
