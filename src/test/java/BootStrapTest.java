import com.chessgear.Bootstrap;
import com.chessgear.game.*;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by neil on 11/8/15
 */
public class BootStrapTest {
    private Bootstrap test;
    @Before
    public void initialize () {
        test = new Bootstrap();

    }

    @Test
    public void testAddUser() {
        test.clearDatabase();
        test = new Bootstrap();
        JsonObject temp = new JsonObject();
        temp.addProperty("email", "nfendle1@jhu.edu");
        temp.addProperty("password", "yoyoyo");
        JsonObject ret = test.createUser(temp);
        assertEquals(ret.get("email").getAsString(), "nfendle1@jhu.edu");
        ret = test.createUser(temp);
        //asserts
        assertEquals(ret.get("why").getAsString(), "User already exists");
        assertEquals(ret.get("status").getAsInt(), 409);
    }

}
