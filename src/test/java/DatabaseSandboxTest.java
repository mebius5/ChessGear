import com.chessgear.data.*;
import com.chessgear.server.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ran on 12/12/2015.
 */
public class DatabaseSandboxTest {

    private String pgn = "[Event \"Hourly Bullet Arena\"]\n" +
            "[Site \"http://lichess.org/c9O2636Z\"]\n" +
            "[Date \"2015.12.01\"]\n" +
            "[White \"lufo\"]\n" +
            "[Black \"Fins\"]\n" +
            "[Result \"0-1\"]\n" +
            "[WhiteElo \"1983\"]\n" +
            "[BlackElo \"2644\"]\n" +
            "[PlyCount \"84\"]\n" +
            "[Variant \"Standard\"]\n" +
            "[TimeControl \"60+0\"]\n" +
            "[ECO \"B01\"]\n" +
            "[Opening \"Scandinavian Defence, Mieses-Kotroc Variation\"]\n" +
            "[Termination \"Normal\"]\n" +
            "[Annotator \"lichess.org\"]\n" +
            "\n" +
            "1. e4 d5?! { (0.20 → 0.72) Inaccuracy. The best move was e6. } (1... e6 2. d4 d5 3. exd5 exd5 4. Nf3 Be7 5. Bd3 Nf6 6. O-O O-O 7. Be3 Re8 8. c3 Nc6 9. Nbd2) 2. exd5 Qxd5 { Scandinavian Defence, Mieses-Kotroc Variation } 3. Nc3 Qd8 4. Nf3 Nf6 5. d4 c6 6. Bf4 Bg4 7. h3 Bxf3 8. Qxf3 e6 9. Be3 Bb4 10. Bd3 Qd5?! { (0.43 → 1.08) Inaccuracy. The best move was Nbd7. } (10... Nbd7 11. O-O Bd6 12. Ne4 Nxe4 13. Bxe4 Nf6 14. Bd3 O-O 15. c4 Bc7 16. Rad1 Qd6 17. Qg3 Qe7 18. f4) 11. Qxd5?! { (1.08 → 0.38) Inaccuracy. The best move was Qg3. } (11. Qg3 Nh5 12. Qg4 Nd7 13. O-O Bxc3 14. bxc3 Nhf6 15. Qg3 c5 16. c4 Qc6 17. c3 Rd8 18. Rae1 O-O) 11... cxd5 12. Bd2 Nc6 13. a3 Bd6 14. Ne2 a6 15. c3 b5 16. O-O O-O 17. Rfd1 Rab8?! { (0.00 → 0.54) Inaccuracy. The best move was Na5. } (17... Na5 18. Ra2 Nb3 19. Be3 Rfb8 20. f3 Nd7 21. Bc2 Na5 22. Raa1 g6 23. b3 Nc6 24. Rdb1 a5 25. a4) 18. Be3 Rfc8 19. Nc1?! { (0.46 → -0.14) Inaccuracy. The best move was a4. } (19. a4 bxa4 20. Bxa6 Rc7 21. Rxa4 Rxb2 22. Bd3 Rc8 23. Rda1 g6 24. Ra8 Rxa8 25. Rxa8+ Nb8 26. Bh6 Nd7) 19... Na5 20. b3?! { (-0.17 → -0.98) Inaccuracy. The best move was Ne2. } (20. Ne2 Nc4 21. Bc1 Ne4 22. g4 a5 23. f3 Ng3 24. Nxg3 Bxg3 25. b3 Nb6 26. Bd2 a4 27. Rab1 axb3) 20... g6?! { (-0.98 → 0.00) Inaccuracy. The best move was Rxc3. } (20... Rxc3 21. Rb1 Rcc8 22. Ne2 Bxa3 23. Bd2 Nc4 24. Bf4 Rb6 25. bxc4 dxc4 26. Bc2 b4 27. Ra1 Nd5 28. Bg3) 21. Bg5 Nd7 22. Be3? { (0.18 → -1.01) Mistake. The best move was a4. } (22. a4 bxa4 23. Rxa4 Nxb3 24. Rxa6 Nxc1 25. Rxc1 Rb6 26. Ra7 Nb8 27. Ra8 Kg7 28. g3 Rb3 29. Bd2 Rc7) 22... f5? { (-1.01 → 0.12) Mistake. The best move was Rxc3. } (22... Rxc3 23. Rb1 Rcc8 24. Bd2 Nc6 25. Ne2 Nf6 26. a4 Ne4 27. axb5 axb5 28. Rbc1 Ba3 29. Ra1 Nxd2 30. Rxd2) 23. Bd2 Kf7 24. b4?! { (0.50 → -0.39) Inaccuracy. The best move was a4. } (24. a4 bxa4 25. b4 Nc4 26. Bxc4 Rxc4 27. Rxa4 Ra8 28. Nd3 Nb6 29. Ra2 Rcc8 30. Rda1 Nc4 31. Bf4 Bxf4) 24... Nc6?! { (-0.39 → 0.53) Inaccuracy. The best move was Nc4. } (24... Nc4 25. Bxc4 dxc4 26. a4 Ra8 27. Ne2 Nb6 28. a5 Nd5 29. Bh6 Be7 30. Bf4 g5 31. Be5 f4 32. f3) 25. Ne2?! { (0.53 → 0.00) Inaccuracy. The best move was a4. } (25. a4 e5 26. dxe5 Ncxe5 27. axb5 axb5 28. Ra6 Rc6 29. Rxc6 Nxc6 30. Bg5 Be5 31. Ne2 Bf6 32. Bf4 Be5) 25... Nb6 26. Nc1 Be7 27. Nb3 Nd8 28. Nc5 Ra8 29. Bc2?! { (0.62 → 0.11) Inaccuracy. The best move was Re1. } (29. Re1 Nc4 30. Bf4 a5 31. Bg3 g5 32. Bh2 Bxc5 33. bxc5 Nc6 34. Reb1 b4 35. axb4 axb4 36. cxb4 Nxd4) 29... Nc4 30. a4 bxa4 31. Bxa4 a5 32. Bd7 Rc7 33. bxa5? { (0.11 → -2.80) Mistake. The best move was Bb5. } (33. Bb5 Nxd2 34. Rxd2 e5 35. Nd7 exd4 36. Rxd4 Rca7 37. bxa5 Rxa5 38. Rxa5 Rxa5 39. Rxd5 Ne6 40. Bc4 Rxd5) 33... Bxc5 34. dxc5?! { (-2.86 → -3.43) Inaccuracy. The best move was Bb5. } (34. Bb5 Nxd2 35. Rxd2 Bd6 36. Rc2 Nc6 37. Rca2 Na7 38. Ba6 Rxc3 39. Bb7 Rb8 40. a6 Rb3 41. Ra4 Nb5) 34... Rxd7 35. Be3?! { (-3.60 → -4.13) Inaccuracy. The best move was a6. } (35. a6 Rda7 36. Bg5 Nc6 37. Rdb1 N6a5 38. g4 Rxa6 39. Kg2 R6a7 40. Rb5 Nc6 41. Rb7+ Ke8 42. Rab1 Rxb7) 35... Nc6 36. Bd4?! { (-4.09 → -4.75) Inaccuracy. The best move was a6. } (36. a6 Rda7 37. Bg5 Rxa6 38. Rxa6 Rxa6 39. Rb1 Ra7 40. f4 e5 41. fxe5 Ke6 42. Rb3 N6xe5 43. Rb8 Kd7) 36... Rda7?! { (-4.75 → -4.01) Inaccuracy. The best move was e5. } (36... e5 37. Be3 f4 38. Bc1 Rxa5 39. Rxa5 N6xa5 40. g3 fxg3 41. fxg3 Nb3 42. c6 Rc7 43. Bg5 Rxc6 44. Rxd5) 37. Be5?? { (-4.01 → -8.51) Blunder. The best move was f4. } (37. f4 Rxa5 38. Rxa5 Rxa5 39. Rb1 Ra7 40. Rb5 h6 41. Kf2 g5 42. g3 Ra2+ 43. Kf3 Nd2+ 44. Ke3 Nf1+) 37... N4xe5 38. c4? { (-8.78 → -9.86) Mistake. The best move was a6. } (38. a6 Rxa6 39. Rab1 Ra5 40. f3 Rxc5 41. f4 Nc4 42. Rb7+ Ne7 43. Rd7 Na3 44. Rd2 Nb5 45. Rb2 Ra1+) 38... Nxc4?! { (-9.86 → -9.25) Inaccuracy. The best move was dxc4. } (38... dxc4 39. a6 Rxa6 40. Rac1 Ra5 41. f4 Nd3 42. Rxc4 Nb2 43. Rdc1 Nxc4 44. Rxc4 e5 45. fxe5 Ke6 46. Rc1) 39. Rab1 Rxa5 40. Rb7+ Kf6 41. Rxh7 N4e5?! { (-9.33 → -8.76) Inaccuracy. The best move was Rxc5. } (41... Rxc5 42. Rc7 Ra2 43. h4 e5 44. h5 gxh5 45. Rc8 d4 46. Rf8+ Kg5 47. Rg8+ Kf4 48. Rh8 e4 49. Rxh5) 42. Re1? { (-8.76 → -10.27) Mistake. The best move was Rb7. } (42. Rb7 Rxc5 43. Rb2 Nc4 44. Rb3 e5 45. Rb7 d4 46. Rc7 Ra2 47. g4 Nd6 48. Rd7 Ke6 49. Rc7 Rcc2) 42... Ra1 { White resigns } 0-1";
    private PGNParser parser = null;
    private GameTreeBuilder builder = null;

    @Before
    public void initialize() {
        try {
            this.parser = new PGNParser(pgn);
        } catch (PGNParseException e) {
            e.printStackTrace();
        }

        try {
            this.builder = new GameTreeBuilder(parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {

        DatabaseService service = DatabaseService.getInstanceOf();

        User absox = new User("absox", "password");
        if (!service.userExists(absox.getUsername())) {
            DatabaseWrapper.addUser(absox);

        }

        System.out.println("User properties: ");
        Map<User.Property, String> absoxProperties = service.fetchUserProperties(absox.getUsername());
        for (User.Property property : absoxProperties.keySet()) {
            System.out.println(property.toString() + " : " + absoxProperties.get(property));
        }

        assertTrue(service.hasRoot(absox.getUsername()));
        assertEquals(service.getRoot(absox.getUsername()), 0);

        GameTreeNode rootAbsoxNode = DatabaseWrapper.getGameTreeNode(absox.getUsername(), absox.getGameTree().getRoot().getId());
        System.out.println("Boardstate: " + rootAbsoxNode.getBoardState().toFEN());
        System.out.println("Multiplicity: " + rootAbsoxNode.getMultiplicity());

        DatabaseWrapper.setMultiplicity(absox.getUsername(), 0, rootAbsoxNode.getMultiplicity()+1);


        if (!service.nodeExists(absox.getUsername(), 1)) {
            GameTreeNode childNode = builder.getListOfNodes().get(1);
            childNode.setId(1);
            DatabaseWrapper.addChild(absox.getUsername(), absox.getGameTree().getRoot(), childNode);
        }

        List<Integer> children = service.childrenFrom(absox.getUsername(), 0);
        int child = children.get(0);
        assertEquals(child, 1);

        int parent = service.parentFrom(absox.getUsername(), 1);
        assertEquals(parent, 0);


    }


}
