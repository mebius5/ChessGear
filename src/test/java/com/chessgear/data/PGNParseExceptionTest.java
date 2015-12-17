package com.chessgear.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/***
 * Test for PGNParseException class
 */
public class PGNParseExceptionTest {
    @Test
    public void testPGNParseException(){
        PGNParseException test = new PGNParseException("Test");
        assertEquals(test.getMessage(),"Test");
    }
}
