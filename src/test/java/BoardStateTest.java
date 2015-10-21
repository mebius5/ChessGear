import com.chessgear.game.BoardState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Boardstate unit tests.
 * Created by Ran on 10/21/2015.
 */
public class BoardStateTest {

    @Test
    public void testFEN() {
        BoardState testBoardState = new BoardState();
        testBoardState.setToDefaultPosition();

        assertEquals(testBoardState.toFEN(), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

}
