package com.chessgear.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * Test for Player class
 */
public class PlayerTest {
    @Test
    public void TestToggle(){
        Player player = Player.BLACK;
        assertEquals(player.toggle(),Player.WHITE);
    }
}
