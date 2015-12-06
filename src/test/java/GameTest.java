/**
 * Created by Grady Xiao on 10/24/15.
 * JUnit Test for Game.java
 */

import static org.junit.Assert.*;

import com.chessgear.game.Game;
import com.chessgear.game.Result;
import org.junit.Test;

import java.util.Date;

public class GameTest {
    @Test
    public void GameTreeConstructorTest() {
        String whitePlayerName="David";
        String blackPlayerName="Rob";
        Date date = new Date(2008, 8, 28);
        String pgn ="Sample";
        Result result = Result.WHITE_WIN;
        int id = 5;

        Game test = new Game(whitePlayerName,blackPlayerName,date,pgn,result,id);
        assertEquals(test.getWhitePlayerName(),whitePlayerName);
        assertEquals(test.getBlackPlayerName(),blackPlayerName);
        assertEquals(test.getDateImported(),date);
        assertEquals(test.getPgn(), pgn);
        assertEquals(test.getResult(),result);
        assertEquals(test.getID(),id);
    }
}
