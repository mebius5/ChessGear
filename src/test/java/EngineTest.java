/**
 * Created by GradyXiao on 11/8/15.
 */
import static org.junit.Assert.*;

import com.chessgear.analysis.Engine;
import com.chessgear.analysis.OsUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class EngineTest {

    private String fen;
    private String fen2;
    private OsUtils osUtils;

    private final int moveTime = 5000;
    @Before
    public void initialize(){
        fen = "rnbqk1r1/ppppbppp/5n2/4p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 w q - 6 5";
        fen2 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        osUtils = new OsUtils();
    }

    @Test
    public void testAnalyseFEN(){
        try {
            Engine engine = new Engine(osUtils.getBinaryLocation());

            int i = 0;
            boolean result = false;

            if(engine.analyseFEN(fen, moveTime).getBestMove().contains("f3e5")||
                    engine.analyseFEN(fen, moveTime).getBestMove().contains("b1c3")||
                    engine.analyseFEN(fen, moveTime).getBestMove().contains("d2d4"))
            {
                i++;
            }

            if(engine.analyseFEN(fen2,moveTime).getBestMove().contains("e2e4")||
             engine.analyseFEN(fen2,moveTime).getBestMove().contains("g1f3") ||
                    engine.analyseFEN(fen2,moveTime).getBestMove().contains("d2d4")){
                i++;
            }


            if(i >= 1){
                result = true;
            }

            assertEquals(result, true);

        } catch(Exception e){
            e.printStackTrace();
            fail("Exception should not be thrown during FEN analysis");
        }
    }


    @Test
    public void testTerminateEngine() { //Should catch IOException since you terminated Engine before analysis
        try {
            Engine engine = new Engine(osUtils.getBinaryLocation());
            engine.terminateEngine();
            engine.analyseFEN(fen, moveTime);
            fail("Exception not thrown even though Engine was terminated. ");
        }catch (Exception e){
            //e.printStackTrace();
            assert(e).getClass().equals(IOException.class);
        }
    }
}