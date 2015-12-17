package com.chessgear.data;

import com.chessgear.game.BoardState;
import com.chessgear.server.User;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test for DatabaseWrapper.
 */
public class DatabaseWrapperTest {

    private DatabaseService database;
    private DatabaseWrapper wrapper;

    private FileStorageService fss;

    public void initialize() {
        this.fss = DatabaseServiceTestTool.createFileStorageService();
        DatabaseServiceTestTool.changeGetInstanceOfInDatabaseService(fss.getReferecencedDatabaseService(), DatabaseWrapper.getInstance());
        DatabaseServiceTestTool.changeGetInstanceOfInFileStorageServiceClass(fss);
        this.database = DatabaseService.getInstanceOf();
        this.wrapper = new DatabaseWrapper(database);
    }

    @Test
    public void testAddUser() {
        initialize();
        User absox = new User("absox", "testPassword");
        this.wrapper.addUser(absox);
        assertTrue(this.database.userExists("absox"));
        Map<User.Property, String> userPropertyMap = this.database.fetchUserProperties("absox");
        assertEquals(userPropertyMap.get(User.Property.PASSWORD), "testPassword");
        terminate();
    }

    @Test
    public void testGetGameTree() {
        initialize();
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
        assertEquals(this.wrapper.getGameTreeNode("hedgehogs4me", 1).getBoardState().toFEN(), "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        terminate();
    }

    public void terminate(){
        DatabaseServiceTestTool.destroyFileStorageService(this.fss);
        DatabaseServiceTestTool.putGetInstanceOfBackToNormal(DatabaseWrapper.getInstance());
    }
}
