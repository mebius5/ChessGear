/**
 * Created by GradyXiao on 11/2/15.
 */

import static org.junit.Assert.*;

import com.chessgear.analysis.Engine;
import org.junit.Test;


public class EngineTest {
    Engine engine = new Engine();

    @Test
    public void testAnalyseFEN(){
        String fen = "rnbqk1r1/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 w q - 6 5";
        int moveTime = 5000;
        engine.analyseFEN(fen,moveTime);
        assertEquals(engine.getBestMove(),"f3e5");
    }
}
