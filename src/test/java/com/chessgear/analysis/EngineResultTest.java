package com.chessgear.analysis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * Test for EngineResult.java
 */
public class EngineResultTest {
    /**
     * Test the setCP, setPV, setBestMove, getCp, getPV, getBestMove,
     * and EngineResult of the EngineResult.java
     */
    @Test
    public void testAll(){
        EngineResult engineResult = new EngineResult();
        final double DELTA = 1e-15;
        double cp=128;
        String pv="f3e5";
        String bestMove="f3e5";
        engineResult.setCp(cp);
        engineResult.setPv(pv);
        engineResult.setBestMove(bestMove);
        assertEquals(engineResult.getCp(),cp, DELTA);
        assertEquals(engineResult.getPv(),pv);
        assertEquals(engineResult.getBestMove(),bestMove);
    }
}
