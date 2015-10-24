/**
 * Test case for GameTree.java
 * Created by GradyXiao on 10/24/15.
 */
import static org.junit.Assert.*;

import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeNode;
import org.junit.Test;

public class GameTreeTest {
    @Test
    public void GameTreeConstructorTest() {
        GameTreeNode root = new GameTreeNode(5);
        GameTree test = new GameTree(root);
        assertEquals(test.getRoot(),root);
    }

}
