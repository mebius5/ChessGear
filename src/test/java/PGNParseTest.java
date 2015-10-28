import com.chessgear.data.PGNParseException;
import com.chessgear.data.PGNParser;
import com.chessgear.game.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

import static com.chessgear.data.PGNParser.parseTags;
import static com.chessgear.data.PGNParser.Tag;

/**
 * JUnit Test for PGNParse.java
 * Created by Ran on 10/18/2015.
 */
public class PGNParseTest {

    String testString;

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
    public void parseTagTest() {

        String testString = "[Event \"Hourly Bullet Arena\"]";
        List<Tag> tags = parseTags(testString);
        Tag tag = tags.get(0);

        assertEquals(tag.getName(), "Event");
        assertEquals(tag.getValue(), "Hourly Bullet Arena");
    }

    @Test
    public void pgnParseInformationTest() {

        try {
            PGNParser pgnParser = new PGNParser(testString);

            assertEquals(pgnParser.getBlackPlayerName(), "Fins");
            assertEquals(pgnParser.getWhitePlayerName(), "gorlee2013");
            assertEquals(pgnParser.getResult(), Result.DRAW);

        } catch (PGNParseException e) {
            fail("PGN Parse exception thrown : " + e.getMessage());
        }

    }

    @Test
    public void stripAnnotationsTest() {
        assertEquals(PGNParser.stripAnnotations(testString), "d4 g6 e3 Bg7 c4 c5 Nc3 cd4 ed4 Nf6 Nf3 O-O Be2 d5 O-O dc4 Bc4 Nc6 Bb3 Bg4 Be3 e6 Qd2 Bf3 gf3 Nb4 Kh1 Nbd5 Bd5 Nd5 Nd5 Qd5 Qe2 Rac8 Rac1 Qa2 Rc8 Rc8 Rc1 Rc1 Bc1 Qb1 Qd2 Bd4 Qd4 Qc1 Kg2 b6 Qd8 Kg7 Qd4 f6 Qd7 Kh6 Qe6 Qg5 Qg4 Qg4 fg4 Kg5 h3 f5 Kg3 fg4 f4 Kf6 hg4 h5 g5 Kf5 Kf3 a5 Ke3 b5 Kd4 Kf4 Kc5 h4 Kb5 h3 Ka5 h2 b4 h1=Q b5 Ke5 b6 Kd6 Ka6 Qa1 Kb7 Qa5 Kc8 Qb6");
    }

    @Test
    public void testExtractTargetSquare() {
        String testString = "Qh6";
        Square result = PGNParser.extractTarget(testString);
        assertEquals(result, "h6");
        assertEquals(PGNParser.extractTarget("e8=Q+"), "e8");
    }

    @Test
    public void testGetPieceType() {
        assertEquals(PGNParser.getPieceType("Ke2"), PieceType.KING);
        assertEquals(PGNParser.getPieceType("e8=Q+"), PieceType.PAWN);
    }

    @Test
    public void testGetDisambiguation() {
        assertEquals(PGNParser.getRankDisambiguation("R3d1"), 3);
        assertEquals(PGNParser.getFileDisambiguation("Ned2"), 'e');
    }

    @Test
    public void testPromotionType() {
        assertEquals(PGNParser.getPromotionType("e8=Q"), PieceType.QUEEN);
        assertEquals(PGNParser.getPromotionType("a1=N"), PieceType.KNIGHT);
    }

    @Test
    public void parseMovesTest() {
        try {

            String testPGN1 = "[Event \"Hourly Bullet Arena\"]\n" +
                    "[Site \"http://lichess.org/t3yPcgEw\"]\n" +
                    "[Date \"2015.10.25\"]\n" +
                    "[White \"ENERGIE73\"]\n" +
                    "[Black \"Fins\"]\n" +
                    "[Result \"0-1\"]\n" +
                    "[WhiteElo \"2218\"]\n" +
                    "[BlackElo \"2560\"]\n" +
                    "[PlyCount \"78\"]\n" +
                    "[Variant \"Standard\"]\n" +
                    "[TimeControl \"60+0\"]\n" +
                    "[ECO \"D06\"]\n" +
                    "[Opening \"Queen's Gambit Declined, Marshall Defence\"]\n" +
                    "[Termination \"Time forfeit\"]\n" +
                    "[Annotator \"lichess.org\"]\n" +
                    "\n" +
                    "1. d4 d5 2. c4 Nf6 { Queen's Gambit Declined, Marshall Defence } 3. Nc3 c6 4. Nf3 e6 5. Bg5 Nbd7 6. e3 Qa5 7. Nd2 Bb4 8. Qc2 c5 9. dxc5 O-O 10. Nb3 Qa4 11. Bd3 dxc4 12. Bxc4 Bxc3+ 13. Qxc3 Ne4 14. Qc2? { (0.18 → -0.94) Mistake. The best move was Qd4. } (14. Qd4 Qb4+ 15. Kf1 Ndxc5 16. Nxc5 Qxc4+ 17. Qxc4 Nd2+ 18. Ke2 Nxc4 19. Rhc1 Nxb2 20. Rab1 b6 21. Rxb2 bxc5 22. Be7 Ba6+ 23. Kf3 Rfe8) 14... Qb4+ 15. Ke2 Nxg5 16. h4 Ne5 17. Rac1? { (-0.62 → -2.48) Mistake. The best move was Nd2. } (17. Nd2 Nh3 18. Rxh3 Bd7 19. Rc1 Qxc5 20. Qc3 Nxc4 21. Nxc4 Rac8 22. Rg3 f6 23. Kf1 Bb5 24. b3 Qh5 25. Rh3 Qg4) 17... Nxc4?? { (-2.48 → 0.64) Blunder. The best move was b5. } (17... b5 18. Bd3 Qg4+ 19. Kf1 Bb7 20. Rh2 Ne4 21. Be2 Qf5 22. Bf3 Rfc8 23. Nd4 Nxf3 24. Nxf5 Nxh2+ 25. Kg1 Rxc5 26. Qxc5 Nxc5 27. Rxc5) 18. Qxc4?! { (0.64 → 0.00) Inaccuracy. The best move was hxg5. } (18. hxg5 h6 19. Qxc4 Qxc4+ 20. Rxc4 hxg5 21. Rd1 e5 22. Rcc1 f6 23. Na5 b6 24. Nb3 bxc5 25. Nxc5 Kf7 26. f3 Rh8 27. Kf2 g4) 18... Qxc4+ 19. Rxc4 Bd7 20. hxg5?! { (0.20 → -0.48) Inaccuracy. The best move was Rd4. } (20. Rd4 Bb5+ 21. Kd2 Ne4+ 22. Rxe4 Bc6 23. Rg4 h5 24. Rd4 Bxg2 25. Rg1 Bd5 26. e4 Bxb3 27. axb3 Rfc8 28. b4 a5 29. bxa5 Rxa5) 20... Bb5 21. Nd2 Rad8 22. b3?! { (-0.46 → -1.09) Inaccuracy. The best move was Rc1. } (22. Rc1 Rd5 23. Ne4 b6 24. c6 Rc8 25. Kf3 Bxc4 26. Rxc4 Kf8 27. Rc1 Rf5+ 28. Ke2 Ke7 29. b3 Rd5 30. Kf3 Rb5 31. Rc2 f5) 22... Rd5 23. e4? { (-0.86 → -2.37) Mistake. The best move was Ne4. } (23. Ne4 Bxc4+ 24. bxc4 Rd7 25. f4 b6 26. Rb1 bxc5 27. Kf3 Rc7 28. g4 Rd8 29. Rb5 Kf8 30. Ra5 Ke7 31. Nxc5 Rd2 32. Ne4 Rb2) 23... Rxc5 24. Ke3?! { (-2.22 → -2.96) Inaccuracy. The best move was Rd1. } (24. Rd1 Rxg5 25. Kf3 Bxc4 26. Nxc4 f5 27. e5 b5 28. Nd6 f4 29. Re1 b4 30. Re4 a5 31. Nc4 Rd8 32. Rxf4 Rd3+ 33. Ke4 Rc3) 24... Bxc4 25. Nxc4 Rxg5 26. g3 Rc5 27. Rd1 b5 28. Nd6 Rc3+ 29. Kf4?! { (-2.97 → -3.62) Inaccuracy. The best move was Rd3. } (29. Rd3 Rc2 30. Nxb5 Rxa2 31. Rc3 e5 32. f4 f6 33. Nd6 Rb2 34. Nc8 exf4+ 35. gxf4 Rd8 36. Ne7+ Kf7 37. Nd5 Kg6 38. Kf3 h5) 29... a6 30. Nb7?! { (-3.36 → -4.07) Inaccuracy. The best move was Rd2. } (30. Rd2 Rd8 31. e5 Kf8 32. Kg4 Ke7 33. f4 f6 34. Re2 g6 35. Kh3 Rc1 36. b4 Rg1 37. Ne4 fxe5 38. fxe5 Rdd1 39. Rc2) 30... f6 31. Rd6? { (-3.84 → -5.25) Mistake. The best move was Kg4. } (31. Kg4 f5+ 32. exf5 exf5+ 33. Kf4 Rc2 34. f3 h6 35. Rd5 g5+ 36. Ke3 Rxa2 37. Nc5 f4+ 38. gxf4 gxf4+ 39. Ke4 Re2+ 40. Kd4 Re3) 31... g5+? { (-5.25 → -3.07) Mistake. The best move was h5. } (31... h5 32. e5 fxe5+ 33. Kxe5 Rxf2 34. Rxa6 Rxg3 35. Nc5 h4 36. Rxe6 Rxa2 37. Ne4 Rxb3 38. Re8+ Kf7 39. Rh8 Rh2 40. Nc5 Re3+ 41. Ne4) 32. Kg4 Rc7? { (-2.85 → -1.32) Mistake. The best move was Rc2. } (32... Rc2 33. Rxe6 Kf7 34. Rxa6 Rxf2 35. Kh5 Rh2+ 36. Kg4 Kg6 37. Kf3 Rc8 38. e5 Rc3+ 39. Ke4 Re2+ 40. Kd4 Rf3 41. Rxf6+ Rxf6 42. exf6) 33. Na5? { (-1.32 → -3.52) Mistake. The best move was Nd8. } (33. Nd8 Rc2 34. Nxe6 Re8 35. f4 gxf4 36. gxf4 a5 37. Kf5 Kf7 38. Rd7+ Re7 39. Rd6 Rxa2 40. Nd4 a4 41. Rxf6+ Kg8 42. Ne6 Re8) 33... Kf7?! { (-3.52 → -2.60) Inaccuracy. The best move was Rc2. } (33... Rc2 34. Nc6 Rxf2 35. a4 Rb2 36. Nd4 Rd2 37. axb5 axb5 38. Nxb5 Rxd6 39. Nxd6 Rd8 40. Nb7 Rc8 41. Nd6 Rc3) 34. Nc6 Ra8 35. e5 f5+? { (-2.58 → -0.65) Mistake. The best move was fxe5. } (35... fxe5 36. Nxe5+ Ke7 37. Rb6 Rc2 38. Nc6+ Kf7 39. Ne5+ Kg8 40. f3 Rxa2 41. Nd7 Kf7 42. Rb7 Rg8 43. Ne5+ Kf6 44. Nd7+ Ke7 45. Nc5+) 36. Kxg5 Rg8+ 37. Kf4 Rg4+ 38. Kf3 Rg8 39. Nd8+ Rxd8?! { White forfeits on time (0.00 → 0.50) Inaccuracy. The best move was Ke8. } (39... Ke8 40. Nxe6 Rc3+ 41. Kf4 Rg6 42. Kxf5 Rf3+ 43. Ke4 Rxf2 44. Nc7+ Ke7 45. Nd5+ Kf7 46. Rd7+ Kf8 47. Rd8+ Kf7 48. Rd7+) 0-1";
            PGNParser testParser1 = new PGNParser(testPGN1);
            List<BoardState> boardStates1 = testParser1.getListOfBoardStates();
            assertEquals(boardStates1.get(boardStates1.size() - 1).toFEN(), "3r4/2r2k1p/p2Rp3/1p2Pp2/8/1P3KP1/P4P2/8 w - - 0 40");



            PGNParser parser = new PGNParser(testString);
            List<BoardState> boardStates = parser.getListOfBoardStates();
            assertEquals(boardStates.get(boardStates.size() - 1).toFEN(), "2K5/8/1q1k2p1/6P1/8/8/8/8 w - - 0 48");
        } catch (PGNParseException e) {
            System.err.println(e.getMessage());
            fail();
        }


    }

}
