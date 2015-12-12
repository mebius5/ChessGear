package com.chessgear.server;

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
        Server server = new Server();
        User user = new User("bob","1234485");
        assertFalse(server.userExists(user.getUsername()));

        server.addUser(user);
        assertTrue(server.userExists(user.getUsername()));

        assertEquals(server.getUser(user.getUsername()),user);
    }
}
