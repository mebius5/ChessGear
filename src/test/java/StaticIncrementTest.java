import com.chessgear.game.Game;
import org.junit.Test;

/**
 * Created by Ran on 12/11/2015.
 */
public class StaticIncrementTest {

    private static int counter = 0;

    @Test
    public void testIncrement() {
        for (int c = 0; c < 10; c++) {
            System.out.println(Game.getGameId());
        }
    }

    private static int getCounter() {
        return StaticIncrementTest.counter++;
    }

}
