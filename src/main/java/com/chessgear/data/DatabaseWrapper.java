package com.chessgear.data;

import com.chessgear.analysis.EngineResult;
import com.chessgear.game.BoardState;
import com.chessgear.game.Move;
import com.chessgear.server.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chessgear.data.GameTreeNode.NodeProperties;

/**
 * Wrapper class for DatabaseService.
 * Helper methods for interfacing with the Database singleton.
 * Adds some abstraction.
 * Created by Ran on 12/12/2015.
 */
public final class DatabaseWrapper {

    private DatabaseService service;
    private static DatabaseWrapper instance = new DatabaseWrapper(DatabaseService.getInstanceOf());

    /**
     * Constructs a database wrapper for the given database service.
     * @param service DatabaseService that we're wrapping.ause
     */
    public DatabaseWrapper(DatabaseService service) {
        this.service = service;
    }

    /**
     * Returns static instance of the wrapper.
     * @return Static instance of wrapper.
     */
    public static DatabaseWrapper getInstance() {
        return instance;
    }

    /**
     * Adds a new user to the database.
     * @param user User to add.
     */
    public void addUser(User user) {
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
    public GameTreeNode getGameTreeNode(String username, int id) {
        if (service.nodeExists(username, id)) {
            GameTreeNode result = new GameTreeNode(id);
            Map<NodeProperties, String> properties = service.fetchNodeProperty(username, id);

            /**
             * If at least one of the fields is not blank, then we compute and put in the engine result.
             */
            if (!properties.get(NodeProperties.BESTMOVE).equals("") || !properties.get(NodeProperties.CP).equals("") || !properties.get(NodeProperties.PV).equals("")) {
                EngineResult engineResult = new EngineResult();
                engineResult.setBestMove(properties.get(NodeProperties.BESTMOVE));
                try {
                    engineResult.setCp(Double.parseDouble(properties.get(NodeProperties.CP)));
                } catch (NumberFormatException e) {
                    engineResult.setCp(0);
                }
                engineResult.setPv(properties.get(NodeProperties.PV));
                result.setEngineResult(engineResult);
            }

            result.setBoardState(new BoardState(properties.get(NodeProperties.BOARDSTATE)));
            result.setMultiplicity(Integer.parseInt(properties.get(NodeProperties.MULTIPLICITY)));

            if (!properties.get(NodeProperties.LASTMOVE).equals("")) {
                result.setLastMoveMade(new Gson().fromJson(properties.get(NodeProperties.LASTMOVE), Move.class));
            }

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
    public void setMultiplicity(String username, int id, int multiplicity) {
        service.updateNodeProperty(username, id, NodeProperties.MULTIPLICITY, String.valueOf(multiplicity));
    }

    /**
     * Adds a node to the tree as the child of a node.
     * @param username User who owns the nodes.
     * @param parent Parent node.
     * @param child Child node.
     */
    public void addChild(String username, GameTreeNode parent, GameTreeNode child) {
        addNode(username, child);
        service.addChild(username, parent.getId(), child.getId());
    }

    /**
     * Adds a node to the database.
     * @param username User who owns the node.
     * @param node Node object to add.
     */
    public void addNode(String username, GameTreeNode node) {
        service.addNode(username, node.getId(), NodeProperties.getProperties(node));
    }

    /**
     * Gets the GameTree for the specified user.
     * @param username User to get GameTree for.
     * @return GameTree of user.
     */
    public GameTree getGameTree(String username) {
        // Initialize tree, mapping
        GameTree tree = new GameTree();
        HashMap<Integer, GameTreeNode> nodeMapping = new HashMap<>();

        // Get the root node, and put it into the mapping.
        GameTreeNode root = getGameTreeNode(username, service.getRoot(username));
        if (root == null) {
            throw new NullPointerException("GameTree not found for the user");
        }
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
    private void setChildren(GameTreeNode currentNode, String username, HashMap<Integer, GameTreeNode> nodeMapping) {
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
