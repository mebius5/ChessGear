package com.chessgear.server;

import com.chessgear.data.DatabaseServiceTestTool;
import com.chessgear.data.DatabaseWrapper;
import com.chessgear.data.FileStorageService;
import com.chessgear.server.Server;
import com.chessgear.server.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by GradyXiao on 12/10/15.
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

        DatabaseServiceTestTool.putGetInstanceOfBackToNormal(DatabaseWrapper.getInstance());
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
}
