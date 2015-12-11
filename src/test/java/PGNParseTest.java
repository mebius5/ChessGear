import com.chessgear.data.PGNParseException;
import com.chessgear.data.PGNParser;
import com.chessgear.game.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    //Logger for Engine
    private static final Logger logger = LoggerFactory.getLogger(PGNParseTest.class);

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
        assertEquals(result.toString(), "h6");
        assertEquals(PGNParser.extractTarget("e8=Q+").toString(), "e8");
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

            PGNParser parser = new PGNParser(testString);
            List<BoardState> boardStates = parser.getListOfBoardStates();
            assertEquals(boardStates.get(boardStates.size() - 1).toFEN(), "2K5/8/1q1k2p1/6P1/8/8/8/8 w - - 0 48");

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

            String testPGN2 = "[Event \"Hourly Bullet Arena\"]\n" +
                    "[Site \"http://lichess.org/RaBUFuVW\"]\n" +
                    "[Date \"2015.10.25\"]\n" +
                    "[White \"Fins\"]\n" +
                    "[Black \"r3tard3d\"]\n" +
                    "[Result \"1-0\"]\n" +
                    "[WhiteElo \"2559\"]\n" +
                    "[BlackElo \"1834\"]\n" +
                    "[PlyCount \"75\"]\n" +
                    "[Variant \"Standard\"]\n" +
                    "[TimeControl \"60+0\"]\n" +
                    "[ECO \"A41\"]\n" +
                    "[Opening \"Rat Defence\"]\n" +
                    "[Termination \"Time forfeit\"]\n" +
                    "[Annotator \"lichess.org\"]\n" +
                    "\n" +
                    "1. d4 d6 { Rat Defence } 2. c4 g6 3. Nc3 Bg7 4. Nf3 c6 5. e4 Nd7 6. Bg5 h6 7. Bh4 Ngf6 8. Qd2 g5 9. Bg3 g4?! { (0.07 → 0.62) Inaccuracy. The best move was Nh5. } (9... Nh5 10. Be2 c5 11. d5 O-O 12. a3 a6 13. O-O Nb6 14. Bd3 Nxg3 15. hxg3 Bg4 16. Rfe1 Qc7 17. Rad1 Bd7 18. Nh2 Be5) 10. Nh4 Nh5 11. Nf5 Nxg3?! { (0.83 → 1.53) Inaccuracy. The best move was Bf6. } (11... Bf6 12. Be2 Nxg3 13. hxg3 h5 14. O-O-O Nf8 15. Kb1 Bxf5 16. exf5 Nd7 17. Ne4 Bg7 18. f3 d5 19. cxd5 cxd5 20. Nc3 Nf6 21. Bb5+) 12. hxg3 Bf6 13. Rxh6 Rxh6 14. Qxh6 Qa5?! { (1.61 → 2.42) Inaccuracy. The best move was Nf8. } (14... Nf8 15. Ng7+ Kd7 16. Rd1 Ng6 17. Nh5 Bh8 18. c5 Kc7 19. Bc4 Qg8 20. Nf4 Nxf4 21. gxf4 Be6 22. Bxe6 fxe6 23. Qh5 Rd8 24. Qh4) 15. O-O-O Kd8 16. Bd3?! { (3.12 → 2.14) Inaccuracy. The best move was Qh5. } (16. Qh5 Nb6 17. c5 Na4 18. Nxa4 Qxa4 19. e5 Bxf5 20. Qxf5 Bg7 21. Qg5 Qxa2 22. Qxg7 Qa1+ 23. Kc2 Qa4+ 24. b3 Qa2+ 25. Kc3) 16... Kc7 17. Qf4 e5 18. dxe5? { (2.00 → 0.23) Mistake. The best move was Qd2. } (18. Qd2 Kb8 19. d5 Qd8 20. Ne3 Bg5 21. Be2 Nc5 22. b4 cxd5 23. Qxd5 Ne6 24. Nb5 a5 25. Nxd6 axb4 26. Kb1 Ra5 27. Qd2 Ra4) 18... Bxe5 19. Qxg4? { (0.19 → -1.00) Mistake. The best move was Qd2. } (19. Qd2 Bxc3 20. bxc3 Qa3+ 21. Kb1 Nb6 22. Qb2 Qxb2+ 23. Kxb2 Be6 24. Be2 Rd8 25. Bxg4 Nxc4+ 26. Kc2 Na3+ 27. Kc1 Rh8 28. Ne3 Bxg4) 19... Nf6? { (-1.00 → 0.00) Mistake. The best move was Bxc3. } (19... Bxc3 20. bxc3 Qxc3+ 21. Kb1 Qb4+ 22. Ka1 Ne5 23. Qe2 Bxf5 24. exf5 Re8 25. Qe4 Nxd3 26. Qxd3 Re1 27. g4 Rxd1+ 28. Qxd1 Qxc4 29. f3) 20. Qg7 Bxc3?! { (-0.03 → 0.64) Inaccuracy. The best move was Be6. } (20... Be6 21. Qg5 Bxc3 22. bxc3 Qxc3+ 23. Kb1 Rd8 24. Qd2 Qe5 25. f3 Nd7 26. g4 Qc5 27. Ng7 Ne5 28. Nxe6+ fxe6 29. Bf1 Rh8 30. Qc3) 21. Qxf7+ Bd7 22. bxc3?! { (0.62 → 0.00) Inaccuracy. The best move was Bc2. } (22. Bc2 Bxb2+ 23. Kxb2 Qb4+ 24. Kc1 Qa3+ 25. Kd2 Ng4 26. Ke2 Qxa2 27. Rd2 Ne5 28. Qe7 Qxc4+ 29. Ke1 Rh8 30. Qxd6+ Kb6 31. Qd4+ Qxd4) 22... Qxc3+ 23. Bc2? { (0.00 → -1.85) Mistake. The best move was Kb1. } (23. Kb1 Qb4+ 24. Ka1 Qc3+ 25. Kb1) 23... Nxe4 24. Kb1?! { (-1.62 → -2.37) Inaccuracy. The best move was Qg7. } (24. Qg7 Qa3+ 25. Qb2 Qxb2+ 26. Kxb2 Bxf5 27. Re1 d5 28. g4 Be6 29. Bxe4 dxe4 30. Rxe4 Kd7 31. f3 Rh8 32. c5 Bd5 33. Re2 Rh1) 24... d5? { (-2.37 → 0.00) Mistake. The best move was Nd2+. } (24... Nd2+ 25. Kc1 Nxc4 26. Qg7 Qa3+ 27. Kb1 Re8 28. Ne3 Nxe3 29. fxe3 Qb4+ 30. Ka1 Rxe3 31. Bf5 Qc3+ 32. Qxc3 Rxc3 33. g4 Rg3 34. Rg1) 25. cxd5 cxd5? { (0.00 → 1.82) Mistake. The best move was Qb4+. } (25... Qb4+ 26. Ka1 Qc3+ 27. Kb1) 26. Qxd5? { (1.82 → 0.00) Mistake. The best move was Ne3. } (26. Ne3 Qb4+ 27. Bb3 Nc3+ 28. Ka1 Qf8 29. Nxd5+ Nxd5 30. Rc1+ Kb6 31. Qxd5 Qf6+ 32. Kb1 Qf5+ 33. Qxf5 Bxf5+ 34. Bc2 Be6 35. Be4 Rd8) 26... Nc5?? { (0.00 → 10.31) Blunder. The best move was Qb4+. } (26... Qb4+ 27. Ka1 Qc3+ 28. Kb1) 27. Qd6+ Kc8? { (8.06 → 53.03) Mistake. The best move was Kd8. } (27... Kd8 28. Nd4 Kc8 29. Qf8+ Kc7 30. Qxa8 Qb4+ 31. Bb3 Na4 32. Ne6+ Bxe6 33. Qd8+ Kc6 34. Qe8+ Kb6 35. Qxe6+ Ka5 36. Qe5+ Nc5 37. Ka1) 28. Ne7+ Kd8 29. Nd5 Qa5?! { (53.07 → Mate in 5) Checkmate is now unavoidable. The best move was Qg7. } (29... Qg7 30. Qc7+ Ke8 31. Re1+ Ne6 32. Rxe6+ Bxe6 33. Qxg7 Bxd5 34. Ba4+ Bc6 35. Qxb7 Bxa4 36. Qxa8+ Ke7 37. Qe4+ Kf6 38. Qxa4 Ke5 39. Qxa7) 30. Qe7+?! { (Mate in 5 → 53.00) Lost forced checkmate sequence. The best move was Qf8+. } (30. Qf8+ Be8 31. Nb6+ Qd2 32. Rxd2+ Nd3 33. Rxd3+ Kc7 34. Qd6#) 30... Kc8 31. Qf8+ Qd8 32. Ne7+ Kc7 33. Nd5+?! { (Mate in 10 → 10.17) Lost forced checkmate sequence. The best move was Qf4+. } (33. Qf4+ Kb6 34. Rd6+ Bc6 35. Nd5+ Ka6 36. Qc4+ b5 37. Rxc6+ Qb6 38. Qxc5 Rb8 39. Bf5 Kb7 40. Rxb6+ axb6 41. Qxb6+ Ka8 42. Nc7#) 33... Kc6 34. Qxc5+?? { (11.60 → -5.36) Blunder. The best move was Nb4+. } (34. Nb4+ Kb5 35. a4+ Nxa4 36. Rd5+ Kb6 37. Qh6+ Bc6 38. Rxd8 Nc3+ 39. Kb2 Rxd8 40. Kxc3 a5 41. Qe3+ Kc7 42. Nxc6 bxc6 43. Kc4 Rd7) 34... Kxc5 35. Bb3? { (-5.62 → -6.89) Mistake. The best move was f3. } (35. f3 Qa5 36. g4 Re8 37. Be4 Bc6 38. Rc1+ Kd6 39. Nc3 Kc7 40. Ka1 Qe5 41. Kb1 Rd8 42. Bxc6 bxc6) 35... Kb5? { (-6.89 → -5.69) Mistake. The best move was Bf5+. } (35... Bf5+ 36. Kb2 Qh8+ 37. Ka3 Qf8 38. Kb2 Qg7+ 39. Nc3 Rc8 40. Bc2 Bxc2 41. Kxc2 Kb4 42. Rb1+ Ka5 43. Rb5+ Ka6 44. Rb3 b6 45. Kd3) 36. Kb2? { (-5.69 → -7.01) Mistake. The best move was g4. } (36. g4 Rc8 37. f3 Ka5 38. Nf4 Qf6 39. Nh5 Qh6 40. Bd5 Rc5 41. Be4 Be6 42. g3 Rb5+ 43. Ka1) 36... Ka6? { (-7.01 → -5.91) Mistake. The best move was Rc8. } (36... Rc8 37. f3 Qh8+ 38. Ka3 Qf8+ 39. Kb2 Qg7+ 40. Ka3 Ka5 41. g4 Qe5 42. Rd2 Rc1 43. g5 Qxg5 44. f4 Qg7 45. Nb4 Qe7 46. Rd4) 37. Rc1? { (-5.91 → -8.60) Mistake. The best move was g4. } (37. g4 Ka5 38. f3 Qh8+ 39. Kb1 Rc8 40. Ne3 Qh7+ 41. Kb2 Qg7+ 42. Kb1 Qg6+ 43. Nc2 Be6 44. Rd2 Bc4 45. Kb2 Bxb3 46. axb3 Qf6+) 37... b6?? { (-8.60 → -4.68) Blunder. The best move was Qh8+. } (37... Qh8+ 38. Kb1 Rf8 39. Ne3 Rxf2 40. Rc2 Rxc2 41. Bxc2 Qd4 42. Nd1 Qd2 43. Nb2 Qxg2 44. Nd1 Kb6 45. Nc3 Qxg3 46. Kb2) 38. Nb4+?? { Black forfeits on time (-4.68 → -7.79) Blunder. The best move was Nc7+. } (38. Nc7+ Ka5 39. Nxa8 Qxa8 40. Rc4 Qxg2 41. Rf4 Qf1 42. Kc3 Qe1+ 43. Kd4 Kb5 44. Kd3 Kc5 45. Rc4+ Kd6 46. Rd4+ Kc7 47. Rf4 Be8) 1-0";
            PGNParser testParser2 = new PGNParser(testPGN2);
            List<BoardState> boardStates2 = testParser2.getListOfBoardStates();
            assertEquals(boardStates2.get(boardStates2.size() - 1).toFEN(), "r2q4/p2b4/kp6/8/1N6/1B4P1/PK3PP1/2R5 b - - 1 38");

        } catch (PGNParseException e) {
            logger.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void testGetGameLength() {
        try {
            PGNParser parser = new PGNParser(this.testString);
            assertEquals(parser.getGameLength(), 47);
        } catch (PGNParseException e) {
            logger.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void testGetHalfMove() {
        try {
            PGNParser parser = new PGNParser(this.testString);
            int gameLength = parser.getGameLength();
            Move whiteHalfMove = parser.getHalfMove(Player.WHITE, gameLength);
            assertEquals(whiteHalfMove.getOrigin().toString(), "b7");
            assertEquals(whiteHalfMove.getDestination().toString(), "c8");
            assertEquals(whiteHalfMove.getPieceType(), PieceType.KING);
            assertEquals(whiteHalfMove.getWhoMoved(), Player.WHITE);
            Move blackHalfMove = parser.getHalfMove(Player.BLACK, gameLength);
            assertEquals(blackHalfMove.getOrigin().toString(), "a5");
            assertEquals(blackHalfMove.getDestination().toString(), "b6");
            assertEquals(blackHalfMove.getPieceType(), PieceType.QUEEN);
            assertEquals(blackHalfMove.getWhoMoved(), Player.BLACK);

        } catch (PGNParseException e) {
            logger.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void testInvalidPGNTags(){
        try {
            String failureString ="1. d4 d5 2. c4 Nf6 { Queen's Gambit Declined, Marshall Defence } 3. Nc3 c6 4. Nf3 e6 5. Bg5 Nbd7 6. e3 Qa5 7. Nd2 Bb4 8. Qc2 c5 9. dxc5 O-O 10. Nb3 Qa4 11. Bd3 dxc4 12. Bxc4 Bxc3+ 13. Qxc3 Ne4 14. Qc2? { (0.18 → -0.94) Mistake. The best move was Qd4. } (14. Qd4 Qb4+ 15. Kf1 Ndxc5 16. Nxc5 Qxc4+ 17. Qxc4 Nd2+ 18. Ke2 Nxc4 19. Rhc1 Nxb2 20. Rab1 b6 21. Rxb2 bxc5 22. Be7 Ba6+ 23. Kf3 Rfe8) 14... Qb4+ 15. Ke2 Nxg5 16. h4 Ne5 17. Rac1? { (-0.62 → -2.48) Mistake. The best move was Nd2. } (17. Nd2 Nh3 18. Rxh3 Bd7 19. Rc1 Qxc5 20. Qc3 Nxc4 21. Nxc4 Rac8 22. Rg3 f6 23. Kf1 Bb5 24. b3 Qh5 25. Rh3 Qg4) 17... Nxc4?? { (-2.48 → 0.64) Blunder. The best move was b5. } (17... b5 18. Bd3 Qg4+ 19. Kf1 Bb7 20. Rh2 Ne4 21. Be2 Qf5 22. Bf3 Rfc8 23. Nd4 Nxf3 24. Nxf5 Nxh2+ 25. Kg1 Rxc5 26. Qxc5 Nxc5 27. Rxc5) 18. Qxc4?! { (0.64 → 0.00) Inaccuracy. The best move was hxg5. } (18. hxg5 h6 19. Qxc4 Qxc4+ 20. Rxc4 hxg5 21. Rd1 e5 22. Rcc1 f6 23. Na5 b6 24. Nb3 bxc5 25. Nxc5 Kf7 26. f3 Rh8 27. Kf2 g4) 18... Qxc4+ 19. Rxc4 Bd7 20. hxg5?! { (0.20 → -0.48) Inaccuracy. The best move was Rd4. } (20. Rd4 Bb5+ 21. Kd2 Ne4+ 22. Rxe4 Bc6 23. Rg4 h5 24. Rd4 Bxg2 25. Rg1 Bd5 26. e4 Bxb3 27. axb3 Rfc8 28. b4 a5 29. bxa5 Rxa5) 20... Bb5 21. Nd2 Rad8 22. b3?! { (-0.46 → -1.09) Inaccuracy. The best move was Rc1. } (22. Rc1 Rd5 23. Ne4 b6 24. c6 Rc8 25. Kf3 Bxc4 26. Rxc4 Kf8 27. Rc1 Rf5+ 28. Ke2 Ke7 29. b3 Rd5 30. Kf3 Rb5 31. Rc2 f5) 22... Rd5 23. e4? { (-0.86 → -2.37) Mistake. The best move was Ne4. } (23. Ne4 Bxc4+ 24. bxc4 Rd7 25. f4 b6 26. Rb1 bxc5 27. Kf3 Rc7 28. g4 Rd8 29. Rb5 Kf8 30. Ra5 Ke7 31. Nxc5 Rd2 32. Ne4 Rb2) 23... Rxc5 24. Ke3?! { (-2.22 → -2.96) Inaccuracy. The best move was Rd1. } (24. Rd1 Rxg5 25. Kf3 Bxc4 26. Nxc4 f5 27. e5 b5 28. Nd6 f4 29. Re1 b4 30. Re4 a5 31. Nc4 Rd8 32. Rxf4 Rd3+ 33. Ke4 Rc3) 24... Bxc4 25. Nxc4 Rxg5 26. g3 Rc5 27. Rd1 b5 28. Nd6 Rc3+ 29. Kf4?! { (-2.97 → -3.62) Inaccuracy. The best move was Rd3. } (29. Rd3 Rc2 30. Nxb5 Rxa2 31. Rc3 e5 32. f4 f6 33. Nd6 Rb2 34. Nc8 exf4+ 35. gxf4 Rd8 36. Ne7+ Kf7 37. Nd5 Kg6 38. Kf3 h5) 29... a6 30. Nb7?! { (-3.36 → -4.07) Inaccuracy. The best move was Rd2. } (30. Rd2 Rd8 31. e5 Kf8 32. Kg4 Ke7 33. f4 f6 34. Re2 g6 35. Kh3 Rc1 36. b4 Rg1 37. Ne4 fxe5 38. fxe5 Rdd1 39. Rc2) 30... f6 31. Rd6? { (-3.84 → -5.25) Mistake. The best move was Kg4. } (31. Kg4 f5+ 32. exf5 exf5+ 33. Kf4 Rc2 34. f3 h6 35. Rd5 g5+ 36. Ke3 Rxa2 37. Nc5 f4+ 38. gxf4 gxf4+ 39. Ke4 Re2+ 40. Kd4 Re3) 31... g5+? { (-5.25 → -3.07) Mistake. The best move was h5. } (31... h5 32. e5 fxe5+ 33. Kxe5 Rxf2 34. Rxa6 Rxg3 35. Nc5 h4 36. Rxe6 Rxa2 37. Ne4 Rxb3 38. Re8+ Kf7 39. Rh8 Rh2 40. Nc5 Re3+ 41. Ne4) 32. Kg4 Rc7? { (-2.85 → -1.32) Mistake. The best move was Rc2. } (32... Rc2 33. Rxe6 Kf7 34. Rxa6 Rxf2 35. Kh5 Rh2+ 36. Kg4 Kg6 37. Kf3 Rc8 38. e5 Rc3+ 39. Ke4 Re2+ 40. Kd4 Rf3 41. Rxf6+ Rxf6 42. exf6) 33. Na5? { (-1.32 → -3.52) Mistake. The best move was Nd8. } (33. Nd8 Rc2 34. Nxe6 Re8 35. f4 gxf4 36. gxf4 a5 37. Kf5 Kf7 38. Rd7+ Re7 39. Rd6 Rxa2 40. Nd4 a4 41. Rxf6+ Kg8 42. Ne6 Re8) 33... Kf7?! { (-3.52 → -2.60) Inaccuracy. The best move was Rc2. } (33... Rc2 34. Nc6 Rxf2 35. a4 Rb2 36. Nd4 Rd2 37. axb5 axb5 38. Nxb5 Rxd6 39. Nxd6 Rd8 40. Nb7 Rc8 41. Nd6 Rc3) 34. Nc6 Ra8 35. e5 f5+? { (-2.58 → -0.65) Mistake. The best move was fxe5. } (35... fxe5 36. Nxe5+ Ke7 37. Rb6 Rc2 38. Nc6+ Kf7 39. Ne5+ Kg8 40. f3 Rxa2 41. Nd7 Kf7 42. Rb7 Rg8 43. Ne5+ Kf6 44. Nd7+ Ke7 45. Nc5+) 36. Kxg5 Rg8+ 37. Kf4 Rg4+ 38. Kf3 Rg8 39. Nd8+ Rxd8?! { White forfeits on time (0.00 → 0.50) Inaccuracy. The best move was Ke8. } (39... Ke8 40. Nxe6 Rc3+ 41. Kf4 Rg6 42. Kxf5 Rf3+ 43. Ke4 Rxf2 44. Nc7+ Ke7 45. Nd5+ Kf7 46. Rd7+ Kf8 47. Rd8+ Kf7 48. Rd7+) 0-1";
            PGNParser pgnParser = new PGNParser(failureString);
            fail("Test should have thrown exception due to invalid tags PGN string");
        }catch(Exception e){
            logger.info("Performing testFalsePGN: Should print logger error due to tags. Ignore previous logger error");
            assertEquals(e.getClass(),PGNParseException.class);
        }
    }

    @Test public void testInvalidPGNMoves(){
        try{
            String failureString = "[Event \"Hourly Bullet Arena\"]\n" +
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
                    "[Annotator \"lichess.org\"]\n";
            PGNParser pgnParser = new PGNParser(failureString);
            pgnParser.parseMoves(pgnParser.getPGN());
            fail("Test should have thrown exception due to invalid moves in PGN string");
        } catch(Exception e){
            logger.info("Performing testInvalidPGNMoves: Should print logger error due to invalid moves. Ignore previous logger error");
            assertEquals(e.getClass(),PGNParseException.class);
        }
    }

}
