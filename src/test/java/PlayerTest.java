/**
 * Created by Grady Xiao on 10/24/15.
 * JUnit Test for PlayerTest.java
 */
import static org.junit.Assert.*;
import org.junit.Test;

import com.chessgear.game.Player;

public class PlayerTest {
    @Test
    public void TestToggle(){
        Player player = Player.BLACK;
        assertEquals(player.toggle(),Player.WHITE);
    }
}
