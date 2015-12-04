import com.chessgear.data.GameTreeNode;
import com.chessgear.game.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ran on 12/4/2015.
 */
public class GameTreeNodeJsonTest {

    private GameTreeNode testNode;

    @Before
    public void initialize() {

        testNode = GameTreeNode.rootNode(0);
        GameTreeNode node1 = new GameTreeNode(1);
        node1.setBoardState(new BoardState("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"));
        node1.setLastMoveMade(new Move(Player.WHITE, PieceType.PAWN, new Square("e2"), new Square("e4"), false, null));
        testNode.addChild(node1);
        GameTreeNode node2 = new GameTreeNode(2);
        node2.setBoardState(new BoardState("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1"));
        node2.setLastMoveMade(new Move(Player.WHITE, PieceType.PAWN, new Square("d2"), new Square("d4"), false, null));
        testNode.addChild(node2);
        GameTreeNode node3 = new GameTreeNode(3);
        node3.setBoardState(new BoardState("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1"));
        node3.setLastMoveMade(new Move(Player.WHITE, PieceType.KNIGHT, new Square("g1"), new Square("f3"), false, null));
        testNode.addChild(node3);
    }

    @Test
    public void testChildrenJson() {
        assertEquals(testNode.getChildrenJson(), "[{\"id\":1,\"name\":\"e2e4\"},{\"id\":2,\"name\":\"d2d4\"},{\"id\":3,\"name\":\"g1f3\"}]");
    }

    @Test
    public void testSelfJson() {
        System.out.println(testNode.getJson());
    }
}
