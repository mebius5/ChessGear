package com.chessgear.server;

import com.chessgear.data.DatabaseServiceTestTool;
import com.chessgear.data.DatabaseWrapper;
import com.chessgear.data.FileStorageService;
import org.junit.Test;

import static org.junit.Assert.*;


/***
 * Test for Server class
 */

public class ServerTest {
    @Test
    public void testServer(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        DatabaseServiceTestTool.changeGetInstanceOfInDatabaseService(fss.getReferecencedDatabaseService(), DatabaseWrapper.getInstance());
        DatabaseServiceTestTool.changeGetInstanceOfInFileStorageServiceClass(fss);

        Server server = new Server();
        User user = new User("bob","1234485");
        assertFalse(server.userExists(user.getUsername()));

        server.addUser(user);
        assertTrue(server.userExists(user.getUsername()));

        assertEquals(server.getUser(user.getUsername()),user);

        DatabaseServiceTestTool.destroyFileStorageService(fss);
        DatabaseServiceTestTool.putGetInstanceOfBackToNormal(DatabaseWrapper.getInstance());
    }
}
