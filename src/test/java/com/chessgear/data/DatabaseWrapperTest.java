package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.server.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for DatabaseWrapper.
 * Created by Ran on 12/16/2015.
 */
public class DatabaseWrapperTest {

    private DatabaseService database;
    private DatabaseWrapper wrapper;

    @Before
    public void initialize() {
        this.database = DatabaseServiceTestTool.createDatabase(false);
        this.wrapper = new DatabaseWrapper(database);
    }

    @Test
    public void testAddUser() {
        User absox = new User("absox", "testPassword");
        this.wrapper.addUser(absox);
        assertTrue(this.database.userExists("absox"));
        Map<User.Property, String> userPropertyMap = this.database.fetchUserProperties("absox");
        assertEquals(userPropertyMap.get(User.Property.PASSWORD), "testPassword");
    }

    @Test
    public void testGetGameTree() {
        User hedge = new User("hedgehogs4me", "absoxRules");
        this.wrapper.addUser(hedge);
        assertFalse(this.database.userExists("absox"));
        this.wrapper.setMultiplicity("hedgehogs4me", 0, 1);
        GameTreeNode rootNode = this.wrapper.getGameTreeNode("hedgehogs4me", 0);
        assertEquals(rootNode.getMultiplicity(), 1);

        GameTree gameTree = this.wrapper.getGameTree("hedgehogs4me");
        assertEquals(gameTree.getNodeWithId(0).getBoardState(), rootNode.getBoardState());

        GameTreeNode newNode = new GameTreeNode(1);
        newNode.setBoardState(new BoardState("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));
        this.wrapper.addChild("hedgehogs4me", rootNode, newNode);
        assertEquals(this.wrapper.getGameTreeNode("hedgehogs4me", 1).getBoardState(), "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
    }

}
