/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.casiofx;

import com.duy.casiofx.token.NumberToken;
import com.duy.casiofx.token.Token;

import java.util.ArrayList;

/**
 * Used as an unit within a tree.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class Node<E> {

    private ArrayList<Node<E>> children;
    private E content;
    private Node parent;

    /**
     * Constructs a new Node<> for a tree.
     *
     * @param content The contents of the node
     */
    public Node(E content) {
        this.content = content;
        children = new ArrayList<>();
    }

    /**
     * Adds the given child to this Node.
     *
     * @param child the child Node
     */
    public void addChild(Node<E> child) {
        children.add(child);
        child.parent = this;
    }

    /**
     * Adds the given child to this Node.
     *
     * @param newChildren a list of all the children to be added to this Node
     */
    public void addChildren(ArrayList<Node<E>> newChildren) {
        for (int i = 0; i < newChildren.size(); i++) {
            this.addChild(newChildren.get(i));
        }
    }

    /**
     * Deletes the nth child
     *
     * @param n the index of the child Node to be deleted
     */
    public void delChild(int n) {
        children.remove(n);
    }

    /**
     * Creates a copy this node
     */
    public Node<E> copy() {
        Node<E> newRoot = new Node<>(this.getContent());
        for (int i = 0; i < this.getNumOfChildren(); i++) {
            newRoot.addChild(this.getChildren().get(i).copy());
        }
        return newRoot;
    }

    /**
     * @return The children of this Node
     */
    public ArrayList<Node<E>> getChildren() {
        return children;
    }

    /**
     * @return The number of children in this Node
     */
    public int getNumOfChildren() {
        return children.size();
    }

    /**
     * @return The data that this Node stores
     */
    public E getContent() {
        return content;
    }

    /**
     * @return The parent of this Node
     */
    public Node<E> getParent() {
        return parent;
    }

    /**
     * Prints the tree that this node is the root of
     *
     * @param indent String used to indent the subtrees
     * @param last   Is this the root's last child?
     */
    public void printTree(String indent, boolean last) {
        System.out.print(indent);
        if (last) {
            System.out.print("\\>");
            indent += "  ";
        } else {
            System.out.print("|>");
            indent += "| ";
        }
        if (this.content instanceof Token) {
            if (this.content instanceof NumberToken) {
                if (((NumberToken) this.content).getValue() % 1 != 0) {
                    System.out.println(" " + ((NumberToken) this.content).getValue());
                } else {
                    System.out.println(" " + (int) (((NumberToken) this.content).getValue()));
                }
            } else {
                System.out.println(" " + ((Token) this.content).getSymbol());
            }
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).printTree(indent, i == children.size() - 1);
        }
    }
}
