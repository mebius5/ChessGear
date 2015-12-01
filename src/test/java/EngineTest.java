/**
 * Created by GradyXiao on 11/8/15.
 */
import static org.junit.Assert.*;

import com.chessgear.analysis.Engine;
import com.chessgear.data.PGNParseException;
import org.junit.Test;

import java.io.IOException;


public class EngineTest {

    private String fen = "rnbqk1r1/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 w q - 6 5";
    private String fen2 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private String fen3 = "rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1";
    private final int moveTime = 5000;
    private final String binaryLocation = "./stockfish-6-src/src/./stockfish";

    @Test
    public void testAnalyseFEN(){
        try {
            Engine engine = new Engine(binaryLocation);
            int i = 0;
            boolean result = false;

            if(engine.analyseFEN(fen, moveTime).getBestMove().equals("f3e5")||
                    engine.analyseFEN(fen, moveTime).getBestMove().equals("b1c3")){
                i++;
            }

            if(engine.analyseFEN(fen2,moveTime).getBestMove().equals("e2e4")||
             engine.analyseFEN(fen2,moveTime).getBestMove().equals("g1f3") ||
                    engine.analyseFEN(fen2,moveTime).getBestMove().equals("d2d4")){
                i++;
            }


            if(i >= 1){
                result = true;
            }

            assertEquals(result, true);

        } catch(Exception e){
            fail("Exception should not be thrown during FEN analysis");
        }
    }


    @Test
    public void testTerminateEngine() { //Should catch IOException since you terminated Engine before analysis
        try {
            Engine engine = new Engine("./stockfish-6-src/src/./stockfish");
            engine.terminateEngine();
            engine.analyseFEN(fen, moveTime);
            fail("Exception not thrown even though Engine was terminated");
        }catch (Exception e){
            //e.printStackTrace();
            assert(e).getClass().equals(IOException.class);
        }
    }
}