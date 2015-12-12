package com.chessgear.data;

import com.chessgear.data.PGNParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by GradyXiao on 12/7/15.
 */
public class PGNParseExceptionTest {
    @Test
    public void testPGNParseException(){
        PGNParseException test = new PGNParseException("Test");
        assertEquals(test.getMessage(),"Test");
    }
}
