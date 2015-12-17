package com.chessgear.game;

import com.chessgear.data.PGNParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * Test for Result class
 */

public class ResultTest {
    @Test
    public void testParseResult() throws PGNParseException{
        assertEquals(Result.parseResult("1-0"),Result.WHITE_WIN);
        assertEquals(Result.parseResult("0-1"),Result.BLACK_WIN);
        assertEquals(Result.parseResult("1/2-1/2"),Result.DRAW);

        assertEquals(Result.parseResult("1-0").toString(),"1-0");
        assertEquals(Result.parseResult("0-1").toString(),"0-1");
        assertEquals(Result.parseResult("1/2-1/2").toString(),"1/2-1/2");
    }
}
