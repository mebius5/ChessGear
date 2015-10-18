/*
 *	Author:      Gilbert Maystre
 *	Date:        Oct 18, 2015
 */

package com.chessgear.data;

import java.util.List;

/**
 * General class to represent an n-ary node.
 * 
 * @author gilbert
 */
public abstract class TreeNode<E> {

    /**
     * This methods returns all children from the current node.
     * 
     * @return A list of all chilfren form the current node.
     */
    public abstract List<TreeNode<E>> getChildren();
    
    /**
     * This methods tells wether the node is a leaf (ie has no children).
     * 
     * @return True if the node has no children, else false.
     */
    public abstract boolean isLeaf();
    
    /**
     * This methods adds a child to the current node.
     * 
     * @param child The node to add as a child.
     */
    public abstract void addChild(TreeNode<? extends E> child);
    
    /**
     * This methods returns the content of the current node.
     * 
     * @return The content of the current node.
     */
    public abstract E content();
}
