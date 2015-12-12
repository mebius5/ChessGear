package com.chessgear.data; /**
 * Created by GradyXiao on 10/24/15.
 * JUnit Test for GameTree.java
 */
import com.chessgear.data.*;
import com.chessgear.game.BoardState;
import com.chessgear.game.PieceType;
import com.chessgear.game.Player;
import com.chessgear.server.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/***
 * Test for GameTree.java
 */
public class GameTreeTest {
    private String testPGN;
    private List<GameTreeNode> gameTreeNodes;

    @Before
    public void initialize() {
        try {
            this.testPGN = "[Event \"Hourly Bullet Arena\"]\n" +
                    "[Site \"http://lichess.org/wahbcSCl\"]\n" +
                    "[Date \"2015.11.03\"]\n" +
                    "[White \"SupaHotFiya\"]\n" +
                    "[Black \"Fins\"]\n" +
                    "[Result \"0-1\"]\n" +
                    "[WhiteElo \"1977\"]\n" +
                    "[BlackElo \"2630\"]\n" +
                    "[PlyCount \"128\"]\n" +
                    "[Variant \"Standard\"]\n" +
                    "[TimeControl \"60+0\"]\n" +
                    "[ECO \"A07\"]\n" +
                    "[Opening \"King's Indian Attack, General\"]\n" +
                    "[Termination \"Time forfeit\"]\n" +
                    "[Annotator \"lichess.org\"]\n" +
                    "\n" +
                    "1. Nf3 d5 2. g3 { King's Indian Attack, General } Nf6 3. Bg2 Bg4 4. O-O Bxf3 5. Bxf3 e6 6. c4 c6 7. cxd5 cxd5 8. d3 Nc6 9. Nd2 Bc5 10. b3 Bd4 11. Rb1 O-O 12. Bb2 Rc8 13. Bxd4 Nxd4 14. Bg2 Qa5 15. e3 Nb5 16. Ra1 Nc3 17. Qf3 Qa3 18. Nb1 Qb2 19. Nxc3 Qxc3 20. Qe2 Qc2 21. Rfe1 b5 22. a4?! { (-0.17 → -0.82) Inaccuracy. The best move was Qxc2. } (22. Qxc2 Rxc2 23. b4 Ng4 24. Bf3 Ne5 25. Be2 Rfc8 26. a4 a6 27. Kg2 Rb2 28. axb5 axb5 29. Rab1 Rcc2 30. Rxb2 Rxb2 31. d4 Nc4) 22... b4 23. h3?! { (-0.80 → -1.42) Inaccuracy. The best move was Rab1. } (23. Rab1 Qxe2 24. Rxe2 Rc3 25. d4 a5 26. Bf3 h5 27. Ree1 g5 28. Be2 g4 29. Red1 Ne4 30. Bd3 f5 31. Kg2 Kf7 32. Bxe4 fxe4) 23... Qxb3 24. Reb1 Qc3 25. Ra2?! { (-1.49 → -2.05) Inaccuracy. The best move was a5. } (25. a5 b3 26. Rb2 Rc5 27. Rab1 Rb5 28. Qd1 Rfb8 29. h4 Ne8 30. Bf3 Nd6 31. Be2 Rb4 32. Kg2 d4 33. e4 R4b5 34. a6 e5) 25... a5 26. Rab2 Rc7 27. g4 Rfc8 28. g5 Nd7 29. h4?! { (-2.32 → -3.31) Inaccuracy. The best move was d4. } (29. d4 Nb6 30. Ra2 Qc6 31. Qd1 Qd7 32. h4 Rc3 33. Raa1 Nc4 34. Rb3 Na3 35. Rxc3 Rxc3 36. h5 Qe7 37. g6 Qg5 38. gxf7+ Kxf7) 29... Ne5? { (-3.31 → -2.22) Mistake. The best move was Nc5. } (29... Nc5 30. Qd1 Qxd3 31. Rd2 Qa3 32. Ra1 Qb3 33. Bf1 Qxd1 34. Rdxd1 f6 35. f3 Kf7 36. Bb5 fxg5 37. hxg5 Kg6 38. f4 Kf7 39. Kg2) 30. d4 Nc4 31. Ra2?? { (-2.50 → -6.02) Blunder. The best move was Rc2. } (31. Rc2 Qxc2 32. Qxc2 Na3 33. Qe2 Nxb1 34. Bf1 Ra8 35. Qb2 Nc3 36. Qb3 Ne4 37. Bd3 Rac8 38. Kg2 Rc3 39. Qb1 Rc1 40. Qb2 Rd1) 31... b3 32. Raa1 b2 33. Ra2 Qc1+ 34. Qf1 Nd2 35. Raxb2 Nxf1? { (-4.93 → -3.58) Mistake. The best move was Nxb1. } (35... Nxb1 36. Rxb1 Qxb1 37. Qxb1 Rc1+ 38. Qxc1 Rxc1+ 39. Bf1 Ra1 40. f4 Rxa4 41. f5 exf5 42. Bb5 Ra1+ 43. Kg2 a4 44. Bc6 a3 45. Bxd5) 36. Bxf1?? { (-3.58 → -7.41) Blunder. The best move was Rxc1. } (36. Rxc1 Rxc1 37. Bxf1 Ra1 38. Rb7 Rxa4 39. Ra7 Ra1 40. Kg2 a4 41. Ba6 Rb8 42. Be2 a3 43. Kg3 a2 44. Kf4 g6 45. h5 gxh5) 36... Qc6 37. Rb6? { (-7.32 → -8.84) Mistake. The best move was Bb5. } (37. Bb5 Qd6 38. Bd3 g6 39. Be2 Rc1+ 40. Kg2 Rxb1 41. Rxb1 Qa3 42. Bb5 Rc2 43. Re1 Qb4 44. Rd1 e5 45. Kf1 e4 46. Kg2 Qe7) 37... Qd7? { (-8.84 → -7.49) Mistake. The best move was Qxa4. } (37... Qxa4 38. R6b2 Rc2 39. Rb8 g6 40. Bd3 Rc1+ 41. Rxc1 Rxb8 42. h5 Qa3 43. Rd1 gxh5 44. Be2 h4 45. Kg2 Qa2 46. Bd3 Rb2) 38. Ba6 Rd8 39. Rb8 h6 40. g6? { (-7.73 → -9.46) Mistake. The best move was Be2. } (40. Be2 hxg5 41. hxg5 Rxb8 42. Rxb8+ Kh7 43. Rb5 Qe7 44. f4 Qa3 45. Kf2 Qxa4 46. Rc5 Rxc5 47. dxc5 Qc2) 40... fxg6 41. R8b6 Kh7?! { (-10.34 → -9.45) Inaccuracy. The best move was Qe7. } (41... Qe7 42. Rb8 Rxb8 43. Rxb8+ Kf7 44. Rb1 Qxh4 45. Rf1 Rc2 46. Bd3 Rd2 47. Ba6 Qg4+ 48. Kh2 Qf3 49. Kg1 Qh3 50. Bb5 Qg4+ 51. Kh1) 42. Bb5?! { (-9.45 → -12.47) Inaccuracy. The best move was Bf1. } (42. Bf1 Qe7 43. h5 gxh5 44. Bg2 Rf8 45. R6b2 Qg5 46. Rf1 Rc3 47. Kh2 Ra3 48. Rb6 Qg4 49. Rb2 Rxa4 50. f3 Qh4+ 51. Kg1) 42... Qf7 43. f4 Qxf4?? { (-14.39 → 2.23) Blunder. The best move was Qf5. } (43... Qf5 44. Be2 Qh3 45. Kf2 Qxh4+ 46. Kg1 Qg3+ 47. Kf1 Qh3+ 48. Ke1 Qxe3 49. R6b3 Qg1+ 50. Kd2 Qxd4+ 51. Bd3 Qxf4+ 52. Ke2 Qxa4 53. Rb7) 44. exf4 Rc2 45. Rxe6 Rd2 46. Re7? { (3.48 → 1.60) Mistake. The best move was Be8. } (46. Be8 Rxd4 47. Bxg6+ Kg8 48. Rbe1 Rxf4 49. Re8+ Rxe8 50. Rxe8+ Rf8 51. Re5 Rd8 52. Kf2 Kf8 53. Ke3 Rb8 54. Ke2 Rd8 55. Kd3 Rb8) 46... Rf8 47. Rc1 Rxf4 48. Rcc7 Rg4+ 49. Kf1 Rf4+?! { (1.60 → 2.15) Inaccuracy. The best move was Rxh4. } (49... Rxh4 50. Ke1 Rdxd4 51. Rxg7+ Kh8 52. Rh7+ Kg8 53. Rcg7+ Kf8 54. Rf7+ Kg8 55. Rhg7+ Kh8 56. Rxg6 Rh1+ 57. Kf2 Rh2+ 58. Kf3 Rh3+ 59. Kg2) 50. Ke1 Rf7?? { (2.07 → Mate in 8) Checkmate is now unavoidable. The best move was Rdxd4. } (50... Rdxd4 51. Rxg7+ Kh8 52. Rh7+ Kg8 53. Rcg7+ Kf8 54. Rxg6 Rfe4+ 55. Kf2 Rf4+ 56. Kg3 Rxh4 57. Rgxh6 Rxh6 58. Rxh6 Ke7 59. Ra6 Re4 60. Kf3) 51. Rxf7 Rxd4 52. Ke2 Re4+ 53. Kf2 Re7 54. Kg3?! { (Mate in 4 → 15.80) Lost forced checkmate sequence. The best move was Rfxe7. } (54. Rfxe7 Kh8 55. Rxg7 h5 56. Rge7 d4 57. Rc8#) 54... Rxf7 55. Rxf7 Kg8 56. Rxg7+?? { (48.42 → 2.43) Blunder. The best move was Rd7. } (56. Rd7 Kf8 57. Rxd5 Kf7 58. Bd3 Ke6 59. Rxa5 Ke7 60. Ra7+ Kd6 61. Rxg7 Kc5 62. Rxg6 Kb4 63. Rxh6 Kxa4 64. Kf4 Kb4 65. Rd6 Kb3) 56... Kxg7 57. Kf3 Kf6 58. Kf2? { (2.11 → 0.83) Mistake. The best move was Kf4. } (58. Kf4 g5+ 59. hxg5+ hxg5+ 60. Kg4 Ke5 61. Kxg5 d4 62. Bd3 Kd6 63. Kf4 Kc5 64. Kf3 Kb4 65. Bb5 Kb3 66. Kf2 Kc2 67. Ke2 Kc3) 58... g5 59. Kg3 gxh4+? { (1.20 → 2.51) Mistake. The best move was d4. } (59... d4 60. hxg5+ Kxg5 61. Bd3 h5 62. Be2 h4+ 63. Kh3 Kf5 64. Kxh4 Kf4 65. Bg4 Ke4 66. Kg3 Ke3 67. Bh5 Ke4 68. Kf2 Kd3 69. Kf3) 60. Kg2? { (2.51 → 0.00) Mistake. The best move was Kxh4. } (60. Kxh4 Ke5 61. Kh5 d4 62. Kxh6 Ke4 63. Kg5 d3 64. Bc6+ Ke3 65. Kg4 Kd2 66. Kf4 Kc1 67. Bf3 Kc2 68. Ke3 d2 69. Bh5 Kc3) 60... Kf5 61. Kg1 Ke4 62. Kf1 Ke3 63. Kg1? { (-1.18 → -3.62) Mistake. The best move was Bd7. } (63. Bd7 Kd2 64. Bf5 d4 65. Kg1 Kc3 66. Kf2 d3 67. Ke1 Kc2 68. Kf2 Kd2 69. Kf3 Kc3 70. Ke3 d2 71. Ke2 h3 72. Kd1 h2) 63... d4 64. Kh2?? { (-6.06 → -48.87) Blunder. The best move was Kf1. } (64. Kf1 Kd2 65. Bd7 d3 66. Bf5 h3 67. Be4 h2 68. Kg2 h1=B+ 69. Kxh1 Ke2 70. Kh2 d2 71. Kg3 Ke1 72. Bc2 d1=Q 73. Bxd1 Kxd1) 64... d3 { White forfeits on time } 0-1";
            PGNParser parser = new PGNParser(this.testPGN);
            GameTreeBuilder testBuilder = new GameTreeBuilder(parser.getListOfBoardStates(), parser.getWhiteHalfMoves(), parser.getBlackHalfMoves());
            gameTreeNodes = testBuilder.getListOfNodes();
        } catch(Exception e){
            e.printStackTrace();
            fail("Exception is not supposed to be thrown");
        }
    }

    @Test
    public void testGameTree() {
        try {

            FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
            DatabaseServiceTestTool.changeGetInstanceOfInDatabaseService(fss.getReferecencedDatabaseService());
            DatabaseServiceTestTool.changeGetInstanceOfInFileStorageServiceClass(fss);

            //Tests GameTree() and getRoot()
            GameTree gameTree = new GameTree(); //Tests GameTree constructor
            assertEquals(gameTree.getRoot(),GameTreeNode.rootNode(0));

            //Tests setNodeIdCounter()
            gameTree.setNodeIdCounter(8);

            String username = "nothing";
            User user = User.registerNewUser("Nothing","Nothing");
            gameTree.addGame(gameTreeNodes, user.getUsername());
            gameTree.addGame(gameTreeNodes, user.getUsername());
            assertEquals(gameTree.getRoot().getMultiplicity(),2);

            BoardState startingBoardState = gameTree.getRoot().getBoardState();
            assertEquals(startingBoardState.toFEN(), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

            assertEquals(gameTree.getNodeWithId(1),null);

            //Test setRoot(), getRoot()
            gameTree.setRoot(gameTreeNodes.get(0));
            assertEquals(gameTree.getRoot(),gameTreeNodes.get(0));

            //Test containsNode(), getNodeWithId()
            assertEquals(gameTree.containsNode(8), true);
            assertEquals(gameTree.getNodeWithId(8).getBoardState().toFEN(), "rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1");
            assertEquals(gameTree.getNodeWithId(8).getLastMoveMade().getWhoMoved(), Player.WHITE);
            assertEquals(gameTree.getNodeWithId(8).getLastMoveMade().getPieceType(), PieceType.KNIGHT);
            assertEquals(gameTree.getNodeWithId(8).getLastMoveMade().getDestination().toString(), "f3");

            //Test the setNodeMapping() and getNodeMapping()
            HashMap<Integer, GameTreeNode> hashMap = new HashMap<>();
            hashMap.put(3,gameTreeNodes.get(3));
            gameTree.setNodeMapping(hashMap);
            assertEquals(gameTree.getNodeMapping(),hashMap);

            //Hash map of gameTree should only have 3 in it, so 0 should return false
            assertTrue(gameTree.containsNode(3));
            assertFalse(gameTree.containsNode(0));

            DatabaseServiceTestTool.destroyFileStorageService(fss);

            DatabaseServiceTestTool.changeGetInstanceOfInDatabaseService(null);
            DatabaseServiceTestTool.changeGetInstanceOfInFileStorageServiceClass(null);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
