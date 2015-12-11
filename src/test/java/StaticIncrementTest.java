import com.chessgear.game.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ran on 12/11/2015.
 */
public class StaticIncrementTest {

    @Test
    public void testIncrement() {
        for (int c = 0; c < 10; c++) {
            assertEquals(Game.getNextGameId(), c);
        }
    }

}
