/**
 * Created by GradyXiao on 10/24/15.
 */
import static org.junit.Assert.*;

import com.chessgear.data.PGNParseException;
import com.chessgear.game.Result;
import org.junit.Test;

public class ResultTest {
    @Test
    public void testParseResult() throws PGNParseException{
        assertEquals(Result.parseResult("1-0"),Result.WHITE_WIN);
        assertEquals(Result.parseResult("0-1"),Result.BLACK_WIN);
        assertEquals(Result.parseResult("1/2-1/2"),Result.DRAW);
    }
}
