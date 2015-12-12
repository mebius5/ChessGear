package com.chessgear.data;

import com.chessgear.analysis.EngineResult;
import com.chessgear.game.BoardState;
import com.chessgear.server.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper methods for interfacing with the Database.
 * Created by Ran on 12/12/2015.
 */
public class DatabaseWrapper {

    private static DatabaseService service = DatabaseService.getInstanceOf();

    /**
     * Adds a new user to the database.
     * @param user User to add.
     */
    public static void addUser(User user) {
        // Adds the entry for the user
        service.addUser(user.getUsername(), User.Property.getProperties(user));
        // Creates a new tree for them.
        GameTreeNode root = user.getGameTree().getRoot();
        addNode(user.getUsername(), root);
        service.addTree(user.getUsername(), root.getId());
    }

    /**
     * Grabs information from the database and puts into an object, without references to parents or children.
     * @param username User who owns the node.
     * @param id Id of the node.
     * @return GameTreeNode object containing the information contained therein.
     */
    public static GameTreeNode getGameTreeNode(String username, int id) {
        if (service.nodeExists(username, id)) {
            Map<GameTreeNode.NodeProperties, String> properties = service.fetchNodeProperty(username, id);
            EngineResult engineResult = new EngineResult();
            engineResult.setBestMove(properties.get(GameTreeNode.NodeProperties.BESTMOVE));
            try {
                engineResult.setCp(Double.parseDouble(properties.get(GameTreeNode.NodeProperties.CP)));
            } catch (NumberFormatException e) {
                engineResult.setCp(0);
            }
            engineResult.setPv(properties.get(GameTreeNode.NodeProperties.PV));
            GameTreeNode result = new GameTreeNode(id);
            result.setEngineResult(engineResult);
            result.setBoardState(new BoardState(properties.get(GameTreeNode.NodeProperties.BOARDSTATE)));
            result.setMultiplicity(Integer.parseInt(properties.get(GameTreeNode.NodeProperties.MULTIPLICITY)));
            return result;
        } else {
            return null;
        }
    }

    /**
     * Sets the multiplicity of a node.
     * @param username User who owns the node.
     * @param id Id of the node.
     * @param multiplicity New multiplicity of the node.
     */
    public static void setMultiplicity(String username, int id, int multiplicity) {
        service.updateNodeProperty(username, id, GameTreeNode.NodeProperties.MULTIPLICITY, String.valueOf(multiplicity));
    }

    /**
     * Adds a node to the tree as the child of a node.
     * @param username User who owns the nodes.
     * @param parent Parent node.
     * @param child Child node.
     */
    public static void addChild(String username, GameTreeNode parent, GameTreeNode child) {
        addNode(username, child);
        service.addChild(username, parent.getId(), child.getId());
    }

    /**
     * Adds a node to the database.
     * @param username User who owns the node.
     * @param node Node object to add.
     */
    public static void addNode(String username, GameTreeNode node) {
        service.addNode(username, node.getId(), GameTreeNode.NodeProperties.getProperties(node));
    }

    /**
     * Gets the GameTree for the specified user.
     * @param username User to get GameTree for.
     * @return GameTree of user.
     */
    public static GameTree getGameTree(String username) {
        // Initialize tree, mapping
        GameTree tree = new GameTree();
        HashMap<Integer, GameTreeNode> nodeMapping = new HashMap<>();

        // Get the root node, and put it into the mapping.
        GameTreeNode root = getGameTreeNode(username, service.getRoot(username));
        tree.setRoot(root);
        nodeMapping.put(root.getId(), root);

        // Recursively set the children
        setChildren(root, username, nodeMapping);
        tree.setNodeMapping(nodeMapping);

        // Sets the node id counter to 1 more than the highest id in the mapping.
        int max = 0;
        for (Integer i : nodeMapping.keySet()) {
            if (i > max) max = i;
        }
        tree.setNodeIdCounter(max + 1);

        return tree;
    }

    /**
     * Recursive method to get children of the current node from the database.
     * @param currentNode Current node we're getting children for.
     * @param username User to whom the nodes belong.
     * @param nodeMapping Nodemapping which we're adding to.
     */
    private static void setChildren(GameTreeNode currentNode, String username, HashMap<Integer, GameTreeNode> nodeMapping) {
        // Gets the list of child ids.
        List<Integer> children = service.childrenFrom(username, currentNode.getId());

        // For each child:
        for (Integer i : children) {
            // Get the node, and add it as a child of the current node.
            GameTreeNode newNode = getGameTreeNode(username, i);
            currentNode.addChild(newNode);
            // Put it into the node mapping.
            nodeMapping.put(newNode.getId(), newNode);

            // Call for all its children.
            setChildren(newNode, username, nodeMapping);
        }
    }

}
