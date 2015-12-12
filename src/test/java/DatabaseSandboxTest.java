import com.chessgear.data.DatabaseService;
import com.chessgear.data.GameTreeNode;
import com.chessgear.server.User;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ran on 12/12/2015.
 */
public class DatabaseSandboxTest {

    @Test
    public void test() {

        DatabaseService service = DatabaseService.getInstanceOf();

        User absox = new User("absox", "password");
        if (!service.userExists(absox.getUsername())) {
            System.out.println("Adding user absox!");
            service.addUser(absox.getUsername(), User.Property.getProperties(absox));

        }

        assertTrue(service.userExists(absox.getUsername()));

        Map<User.Property, String> absoxProperties = service.fetchUserProperties(absox.getUsername());
        for (User.Property property : absoxProperties.keySet()) {
            System.out.println(property.toString() + " : " + absoxProperties.get(property));
        }

        if (!service.hasRoot(absox.getUsername())) {
            System.out.println("Adding tree for user absox");
            GameTreeNode rootNode = GameTreeNode.rootNode(0);
            service.addNode(absox.getUsername(), 0, GameTreeNode.NodeProperties.getProperties(rootNode));
            service.addTree(absox.getUsername(), 0);
        }

        assertTrue(service.hasRoot(absox.getUsername()));

        assertEquals(service.getRoot(absox.getUsername()), 0);

    }

}
