package com.chessgear;

import com.chessgear.data.*;

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

    public static void main(String[] args) {

        port(PORT);
        ipAddress(ADDRESS);
        staticFileLocation("/html");

        try {
            GameTree tree = new GameTree();
            PGNParser parser = new PGNParser(sampleGame);
            GameTreeBuilder b = new GameTreeBuilder(parser.getListOfBoardStates(), parser.getWhiteHalfMoves(), parser.getBlackHalfMoves());
            tree.addGame(b.getListOfNodes());

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

                    return result.toString();

                } else {
                    response.status(404);
                    return "Node does not exist!";
                }
            });

            put("chessgear/api/games/import", "application/json", (request, response) -> {
                return ""; // TODO
            });
        } catch (PGNParseException e) {
            System.exit(1);
        }


    }
}
