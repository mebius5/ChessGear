package com.chessgear.game; /**
 * Created by Grady Xiao on 10/24/15.
 * JUnit Test for Game.java
 */

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import com.chessgear.data.DatabaseService;
import com.chessgear.data.PGNParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTest {
    String testString;

    //Logger for Engine
    private static final Logger logger = LoggerFactory.getLogger(GameTest.class);

    @Before
    public void initialize() {
        this.testString = "[Event \"Hourly Bullet Arena\"]\n" +
                "[Site \"http://lichess.org/egYksmzg\"]\n" +
                "[Date \"2015.10.04\"]\n" +
                "[White \"gorlee2013\"]\n" +
                "[Black \"Fins\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "[WhiteElo \"1883\"]\n" +
                "[BlackElo \"2492\"]\n" +
                "[PlyCount \"94\"]\n" +
                "[Variant \"Standard\"]\n" +
                "[TimeControl \"60+0\"]\n" +
                "[ECO \"A40\"]\n" +
                "[Opening \"Modern Defence, Queen Pawn Fianchetto\"]\n" +
                "[Termination \"Normal\"]\n" +
                "[Annotator \"lichess.org\"]\n" +
                "\n" +
                "1. d4 g6 { Modern Defence, Queen Pawn Fianchetto } 2. e3 Bg7 3. c4 c5 4. Nc3 cxd4 5. exd4 Nf6 6. Nf3 O-O 7. Be2 d5 8. O-O dxc4 9. Bxc4 Nc6 10. Bb3 Bg4 11. Be3 e6 12. Qd2?! { (-0.18 → -0.94) Inaccuracy. The best move was h3. } (12. h3 Bxf3 13. Qxf3 Nxd4 14. Bxd4 Qxd4 15. Qxb7 Rab8 16. Qf3 Nh5 17. g3 Qf6 18. Qxf6 Nxf6 19. Rfd1 Rfd8 20. Rac1 Nd7 21. Na4 Nb6) 12... Bxf3 13. gxf3 Nb4?! { (-0.90 → -0.27) Inaccuracy. The best move was Na5. } (13... Na5 14. Bc2 Rc8 15. Rac1 Nc4 16. Qe2 Nh5 17. Ne4 Bxd4 18. Ba4 Nxb2 19. Rxc8 Qxc8 20. Rc1 Qa8 21. Bxd4 Nxa4 22. Ng5) 14. Kh1 Nbd5 15. Bxd5?! { (-0.34 → -1.05) Inaccuracy. The best move was Rac1. } (15. Rac1 a5 16. Qd3 Qd7 17. Qb5 Qxb5 18. Nxb5 a4 19. Bc4 Rfc8 20. Kg2 Rc6 21. Rfe1 Nb6 22. Bd3 Nfd5 23. Rc5 Rac8) 15... Nxd5 16. Nxd5 Qxd5 17. Qe2 Rac8 18. Rac1 Qxa2 19. Rxc8 Rxc8 20. Rc1? { (-1.70 → -2.74) Mistake. The best move was Qb5. } (20. Qb5 Qc4 21. Qxc4 Rxc4 22. Rd1 Kf8 23. d5 Ke7 24. b3 Rc3 25. Bxa7 exd5 26. Rxd5 Rxb3 27. Bc5+ Ke8 28. Be3 b5 29. Kg2 Rb2) 20... Rxc1+ 21. Bxc1 Qb1 22. Qd2 Bxd4 23. Qxd4 Qxc1+ 24. Kg2 b6? { (-2.52 → -1.34) Mistake. The best move was Qg5+. } (24... Qg5+ 25. Kf1 a5 26. f4 Qb5+ 27. Ke1 Qd5 28. Qe5 b5 29. Qxd5 exd5 30. Kd2 Kf8 31. Kd3 Ke7 32. Kd4 Kd6 33. b3 b4) 25. Qd8+ Kg7 26. Qd4+ f6 27. Qd7+ Kh6 28. Qxe6 Qg5+ 29. Qg4? { (-1.42 → -2.44) Mistake. The best move was Kf1. } (29. Kf1 Qe5 30. Qh3+ Qh5 31. Qg3 Qb5+ 32. Kg2 Qg5 33. Kh3 Qe5 34. Kg2 a5 35. Qh3+ Qh5 36. Qd7 b5 37. b3 Qf5 38. Qd4 Qe5) 29... Qxg4+ 30. fxg4 Kg5 31. h3? { (-2.25 → -4.92) Mistake. The best move was Kg3. } (31. Kg3 f5 32. h4+ Kf6 33. Kf4 fxg4 34. Kxg4 a5 35. Kf4 b5 36. Ke4 h5 37. f3 a4 38. f4 b4 39. Kd4 Kf5 40. Kc4 a3) 31... f5? { (-4.92 → -3.06) Mistake. The best move was Kh4. } (31... Kh4 32. f4 a5 33. Kf3 Kxh3 34. g5 fxg5 35. fxg5 Kh4 36. Kf2 Kxg5 37. Kg2 h5 38. b3 Kf4 39. Kf1 h4 40. Kg2 g5) 32. Kg3?? { (-3.06 → -6.95) Blunder. The best move was gxf5. } (32. gxf5 Kxf5 33. Kf3 a5 34. Kg3 h5 35. Kf3 g5 36. Ke2 Kf4 37. Kf1 Kf3 38. Kg1 b5 39. h4 gxh4 40. Kh2 a4 41. Kh3 b4) 32... fxg4 33. f4+?? { (-5.52 → -12.28) Blunder. The best move was hxg4. } (33. hxg4 a5 34. f3 a4 35. Kf2 Kf4 36. g5 Kxg5 37. Ke3 h5 38. Kf2 b5 39. Ke1 Kf4 40. Kf2 b4 41. Ke2 h4 42. Kf2) 33... Kf6?? { (-12.28 → -5.37) Blunder. The best move was gxf3. } (33... gxf3 34. Kxf3 Kh4 35. Kg2 a5 36. Kf3 g5 37. Kg2 b5 38. Kf3 Kxh3 39. Kf2 g4 40. Kf1 Kh2 41. Ke2 g3 42. Kd3 g2 43. Kc3) 34. hxg4 h5? { (-9.14 → -7.04) Mistake. The best move was a5. } (34... a5 35. Kf2 b5 36. Kf3 a4 37. Ke4 h5 38. gxh5 gxh5 39. Kd4 b4 40. Kc4 h4 41. Kxb4 h3 42. Kxa4 h2 43. Ka5 h1=Q 44. b4) 35. g5+? { (-7.04 → -39.76) Mistake. The best move was gxh5. } (35. gxh5 gxh5 36. Kg2 Kf5 37. Kf3 h4 38. Ke3 Kg4 39. Ke2 Kxf4 40. Kf2 Kg5 41. Kg2 a5 42. Kf2 Kg4 43. Kf1 a4 44. Kg2 Kg5) 35... Kf5 36. Kf3 a5 37. Ke3 b5 38. Kd4 Kxf4 39. Kc5 h4 40. Kxb5 h3 41. Kxa5 h2 42. b4 h1=Q 43. b5 Ke5 44. b6 Kd6 45. Ka6 Qa1+?! { (Mate in 4 → Mate in 5) Not the best checkmate sequence. The best move was Kc6. } (45... Kc6 46. Ka5 Kc5 47. Ka4 Kc4 48. Ka5 Qa1#) 46. Kb7 Qa5?! { (Mate in 3 → Mate in 3) Not the best checkmate sequence. The best move was Qg7+. } (46... Qg7+ 47. Ka6 Kc5 48. b7 Qa1#) 47. Kc8 Qxb6?? { Stalemate (Mate in 1 → 0.00) Lost forced checkmate sequence. The best move was Qa8#. } (47... Qa8#) 1/2-1/2";
    }

    @Test
    public void testGame() {
        try {
            PGNParser pgnParser = new PGNParser(testString);
            Game test = new Game(pgnParser);
            assertEquals(test.getWhitePlayerName(), pgnParser.getWhitePlayerName());
            assertEquals(test.getBlackPlayerName(), pgnParser.getBlackPlayerName());
            assertEquals(test.getPgn(), pgnParser.getPGN());
            assertEquals(test.getResult(), pgnParser.getResult());
            
            //we retrieve the internal counter of number of games;
            Field f = Game.class.getDeclaredField("nextGameId");
            f.setAccessible(true);
            int gameId = f.getInt(test);
            f.setAccessible(false);
            
            assertEquals(test.getID(), gameId - 1);
        }catch(Exception e){
            logger.error(e.getMessage());
            fail("Exception should not have been thrown during testGame of GameTest");
        }
    }
}