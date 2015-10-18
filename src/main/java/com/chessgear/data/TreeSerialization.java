/*
 *	Author:      Gilbert Maystre
 *	Date:        Oct 18, 2015
 */

package com.chessgear.data;

//This class will have to move somewhere else
public class TreeSerialization {
    
    private static final char END_OF_CHILDREN = '$';
    
    private TreeNode<String> root;
    
  
    
    public TreeSerialization(TreeNode<String> root){
        this.root = root;
    }
    

    public String serialize(TreeNode<String> n){
        //simple recursive function
        if(n.isLeaf()){
            return n.content().toString();
        }
        else{
            StringBuilder bd = new StringBuilder();
            n.getChildren();
            for(TreeNode<String> no: n.getChildren()){
                bd.append(serialize(no));
            }
            bd.append(END_OF_CHILDREN);
            return bd.toString();
        }
    }

}
