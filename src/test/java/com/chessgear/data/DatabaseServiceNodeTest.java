package com.chessgear.data;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.GameTreeNode;
import com.chessgear.data.GameTreeNode.NodeProperties;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Nov 8, 2015
 */

public class DatabaseServiceNodeTest {

    @Test
    public void testAddNodeReallyAdds(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        //examples with no properties
        db.addNode("gogol", 1, Collections.emptyMap());
        assertTrue(db.nodeExists("gogol", 1));
        assertFalse(db.nodeExists("gogol", 2));
        db.addNode("gogol", 2, Collections.emptyMap());
        assertTrue(db.nodeExists("gogol", 2));
        
        //example with properties
        assertFalse(db.nodeExists("jean", 1));
        Map<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        for(GameTreeNode.NodeProperties p : GameTreeNode.NodeProperties.values())
            prop.put(p, p+"gogol");
        db.addNode("jean", 1, prop);
        assertTrue(db.nodeExists("jean", 1));

        DatabaseServiceTestTool.destroyDatabase(db);
    }

    @Test
    public void testCannotAddTwiceANode(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        try{
            db.addNode("gogol", 1, Collections.emptyMap());
            db.addNode("gogol", 1, Collections.emptyMap());
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
    public void testCannotAddNodeWithInexistentUser(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(false);
        
        try{
            db.addNode("inexistantuser", 1, Collections.emptyMap());
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
    public void testFetchPropertiesNodeWorks(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        HashMap<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        prop.put(NodeProperties.CP, "0.78");
        
        db.addNode("gogol", 1, prop);
        assertEquals(db.fetchNodeProperty("gogol", 1).get(NodeProperties.CP), "0.78");
        
        //a second example with null value
        db.addNode("gogol", 2, Collections.emptyMap());
        assertTrue(db.fetchNodeProperty("gogol", 2).get(NodeProperties.CP) == null);

        db.addNode("gogol", 0, prop);
        assertFalse(db.hasRoot("gogol"));
        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testUpdatePropertyWorks(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        HashMap<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        prop.put(NodeProperties.CP, "0.78");
        
        db.addNode("gogol", 1, prop);
        assertEquals(db.fetchNodeProperty("gogol", 1).get(NodeProperties.CP), "0.78");
        
        db.updateNodeProperty("gogol", 1, GameTreeNode.NodeProperties.CP, "0.333");
        assertEquals(db.fetchNodeProperty("gogol", 1).get(NodeProperties.CP), "0.333");

        DatabaseServiceTestTool.destroyDatabase(db);
        
    }
    
    @Test
    public void testUpdatePropertyThrowsException(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(false);
        
        try{
            db.updateNodeProperty("inexistantuser", 1, GameTreeNode.NodeProperties.CP, "0.333");
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
    public void testRemoveWorks(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(true);
        
        HashMap<GameTreeNode.NodeProperties, String> prop = new HashMap<>();
        prop.put(NodeProperties.CP, "0.78");
        db.addNode("gogol", 1, prop);
        
        assertTrue(db.nodeExists("gogol", 1));
        
        db.deleteNode("gogol", 1);
        
        assertFalse(db.nodeExists("gogol", 1));

        DatabaseServiceTestTool.destroyDatabase(db);
    }
    
    @Test
    public void testRemoveThrowsException(){
        DatabaseService db = DatabaseServiceTestTool.createDatabase(false);
        
        try{
            db.deleteNode("inexistantuser", 1);
            fail();
        }
        catch(IllegalArgumentException e){
            //intentionally left blank
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
        
        // a positive test
        db.deleteNode("jean", 5);
        db.deleteNode("jean", 2);
       
        //this one should fail!
        try{
            db.deleteNode("jean", 3);
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
