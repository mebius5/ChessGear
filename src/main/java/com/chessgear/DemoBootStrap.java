package com.chessgear;

import com.chessgear.analysis.EngineResult;
import com.chessgear.data.GameTree;
import com.chessgear.data.GameTreeBuilder;
import com.chessgear.data.GameTreeNode;
import com.chessgear.data.PGNParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

/**
 * Created by Ran on 11/12/2015.
 */
public class DemoBootStrap {

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";

    static String sampleGame = "e4 e5 Nf3";
    static String sampleGame2 = "d4 e6";
    static String sampleGame3 = "e4 c5 Nf3";

    public static void main(String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");

        try {

            GameTree tree = new GameTree();
            /*
            PGNParser parser = new PGNParser(sampleGame);
            GameTreeBuilder b = new GameTreeBuilder(parser.getListOfBoardStates(), parser.getWhiteHalfMoves(), parser.getBlackHalfMoves());
            tree.addGame(b.getListOfNodes());
            PGNParser parser2 = new PGNParser(sampleGame2);
            GameTreeBuilder b2 = new GameTreeBuilder(parser2.getListOfBoardStates(), parser2.getWhiteHalfMoves(), parser2.getBlackHalfMoves());
            tree.addGame(b2.getListOfNodes());
            PGNParser parser3 = new PGNParser(sampleGame3);
            GameTreeBuilder b3 = new GameTreeBuilder(parser3.getListOfBoardStates(), parser3.getWhiteHalfMoves(), parser3.getBlackHalfMoves());
            tree.addGame(b3.getListOfNodes());
            */

            get("chessgear/api/games/tree/:nodeid", "application/json", (request, response) -> {
                int nodeId = Integer.parseInt(request.params("nodeid"));
                System.out.println("Request received for node " + nodeId);

                if (tree.containsNode(nodeId)) {
                    GameTreeNode currentNode = tree.getNodeWithId(nodeId);
                    String boardState = currentNode.getBoardState().toFEN();
                    List<GameTreeNode> children = currentNode.getChildren();
                    GameTreeNode parent = currentNode.getParent();

                    List<Integer> childIds = new ArrayList<>();
                    for (GameTreeNode currentChildNode : children) {
                        childIds.add(currentChildNode.getId());
                    }

                    StringBuilder result = new StringBuilder();
                    result.append("{ \"boardstate\" : \"");
                    result.append(boardState);
                    result.append("\", \"children\" : [");
                    for (int c = 0; c < childIds.size(); c++) {
                        result.append(childIds.get(c));
                        if (c + 1 != childIds.size()) {
                            result.append(", ");
                        }
                    }
                    result.append("], \"previousNodeId\" : ");

                    if (parent != null) {
                        result.append(parent.getId());
                    } else {
                        result.append("null");
                    }
                    result.append(" }");

                    EngineResult engineResult = currentNode.getEngineResult();
                    if (engineResult != null) {
                        System.out.println("Eval " + engineResult.getCp());
                    }

                    System.out.println(result.toString());
                    return result.toString();

                } else {
                    response.status(404);
                    return "Node does not exist!";
                }
            });

            post("chessgear/api/games/import", "application/json", (request, response) -> {
                System.out.println("Request received for pgn import: " + request.body());
                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(request.body());
                JsonObject jsonObject = element.getAsJsonObject();
                String pgn = jsonObject.get("pgn").getAsString();
                PGNParser currentPgnParser = new PGNParser(pgn);
                GameTreeBuilder currentTreeBuilder = new GameTreeBuilder(currentPgnParser.getListOfBoardStates(), currentPgnParser.getWhiteHalfMoves(), currentPgnParser.getBlackHalfMoves());
               // tree.addGame(currentTreeBuilder.getListOfNodes(), );

                return "Success"; // TODO
            });
        } catch (Exception e) {
            System.exit(1);
        }
    }
}
