/**
 * Created by GradyXiao on 11/8/15.
 */
import static org.junit.Assert.*;

import com.chessgear.analysis.Engine;
import org.junit.Test;

import java.io.IOException;


public class EngineTest {
    private Engine engine = new Engine();
    private String fen = "rnbqk1r1/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 w q - 6 5";
    //private String fen2 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    //private String fen3 = "8/8/p2bpk2/1p6/1P2Q3/P2K4/8/8 b - - 6 55";
    private int moveTime = 5000;

    @Test
    public void testAnalyseFEN(){
        try {
            int i = 0;
            assertEquals(engine.analyseFEN(fen, moveTime).getBestMove(), "f3e5");
            //assertEquals(engine.analyseFEN(fen2,moveTime).getBestMove(), "e2e4");
            //assertEquals(engine.analyseFEN(fen3,moveTime).getBestMove(), "d6e5");
        }catch(Exception e){
            fail();
        }
    }

    @Test
    public void testTerminateEngine() { //Should catch IOException since you terminated Engine before analysis
        try {
            engine.terminateEngine();
            engine.analyseFEN(fen, moveTime);
            fail("Exception not thrown even though Engine was terminated");
        }catch (Exception e){
            //e.printStackTrace();
            assert(e).getClass().equals(IOException.class);
        }
    }
}