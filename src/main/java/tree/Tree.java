package tree;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayDeque;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bertex
 */
public class Tree {

//    TODO: pasar como argumento a las funciones
    private static final int ARRAY_SIZE = 1024 * 1024;
    private static final int NUM_THREADS = 2;

    private int data;
    private Tree leftNode;
    private Tree rightNode;

    public Tree(Tree leftNode, Tree rightNode, int data) {
        this.data = data;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Tree getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Tree leftNode) {
        this.leftNode = leftNode;
    }

    public Tree getRightNode() {
        return rightNode;
    }

    public void setRightNode(Tree rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "Tree{" + "data=" + data + ", leftNode=" + leftNode + ", rightNode=" + rightNode + '}';
    }

    public static Tree[] initArray() {
        Tree[] nodes = new Tree[ARRAY_SIZE];

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Tree(null, null, i);
        }
        return nodes;
    }

    public static Tree createTree(Tree[] nodes) {
        Tree root = nodes[0];
        Tree tmpRoot = root;
        ArrayDeque<Tree> queueNext = new ArrayDeque<>(ARRAY_SIZE / 2);
        int size = nodes.length;
        for (int i = 1; i < size;) {
            tmpRoot.setLeftNode(nodes[i++]);
            if (i == size) {
                break;
            }
            tmpRoot.setRightNode(nodes[i++]);
            queueNext.add(tmpRoot.getLeftNode());
            queueNext.add(tmpRoot.getRightNode());
            tmpRoot = queueNext.pop();
        }
        return root;

    }

    public static Tree randomTree(Tree[] nodes) {
        Tree currNode, tmpRoot;
        boolean left;
        shuffleArray(nodes);
        Tree root = nodes[0];
        Random rnd = new Random();
        for (int i = 1; i < nodes.length; i++) {
            tmpRoot = root;
            do {
                currNode = tmpRoot;
                if (rnd.nextBoolean()) {
                    tmpRoot = currNode.getLeftNode();
                    left = true;
                } else {
                    tmpRoot = currNode.getRightNode();
                    left = false;
                }

            } while (tmpRoot != null);
            if (left) {
                currNode.setLeftNode(nodes[i]);
            } else {
                currNode.setRightNode(nodes[i]);
            }

        }
        return root;

    }

    private static void shuffleArray(Tree[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Tree a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static Tree createConcurrentTree(Tree[] nodes) {
        if (nodes.length < 14) {
            return createTree(nodes);
        }
        int lowIdx, highIdx;
        Tree root = nodes[0];
        Thread myThreads[] = new Thread[NUM_THREADS];
        int itemsByThread = nodes.length / (NUM_THREADS * 2);
        for (int j = 0; j < NUM_THREADS; j++) {
            lowIdx = itemsByThread * j;
            highIdx = itemsByThread * (j + 1);
            if (j == NUM_THREADS - 1) {
                highIdx = (int) Math.ceil(nodes.length / 2);
            }
            myThreads[j] = new Thread(new ThreadConcurrentTree(nodes, lowIdx, highIdx));
            myThreads[j].start();
        }
        for (int j = 0; j < NUM_THREADS; j++) {
            try {
                myThreads[j].join(); //todo add catch exception
            } catch (InterruptedException ex) {
                Logger.getLogger(Tree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return root;
    }

    public static Tree concurrentRandomTree(Tree[] nodes) {
        if (nodes.length < 14) {
            return randomTree(nodes);
        }
        int lowIdx, highIdx;
        shuffleArray(nodes);
        Tree root = nodes[0];
        Thread myThreads[] = new Thread[NUM_THREADS];
        int itemsByThread = nodes.length / (NUM_THREADS * 2);
        for (int j = 0; j < NUM_THREADS; j++) {
            lowIdx = itemsByThread * j;
            highIdx = itemsByThread * (j + 1);
            if (j == NUM_THREADS - 1) {
                highIdx = (int) Math.ceil(nodes.length / 2);
            }
            myThreads[j] = new Thread(new ThreadConcurrentRandomTree(nodes, lowIdx, highIdx));
            myThreads[j].start();
        }
        for (int j = 0; j < NUM_THREADS; j++) {
            try {
                myThreads[j].join(); //todo add catch exception
            } catch (InterruptedException ex) {
                Logger.getLogger(Tree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Tree tmpRoot, currNode;
        Random rnd = new Random();
        boolean left;
        for (int i = 0; i < NUM_THREADS;) {
            tmpRoot = root;
            do {
                currNode = tmpRoot;
                if (rnd.nextBoolean()) {
                    tmpRoot = currNode.getLeftNode();
                    left = true;
                } else {
                    tmpRoot = currNode.getRightNode();
                    left = false;
                }

            } while (tmpRoot != null);

            if (left) {
                currNode.setLeftNode(nodes[itemsByThread * ++i]);
            } else {
                currNode.setRightNode(nodes[itemsByThread * ++i]);
            }
        }
        return root;

    }

    public boolean equals(Tree tree) {
        if (tree == this) {
            return true;
        }
        if (tree == null) {
            return false;
        }
        if (tree.leftNode != this.leftNode && tree.leftNode == null) {
            return false;
        }
        if (tree.rightNode != this.rightNode && tree.rightNode == null) {
            return false;
        }

        return this.data == tree.data && (tree.leftNode == this.leftNode || tree.leftNode.equals(this.leftNode))
                && (tree.rightNode == this.rightNode || tree.rightNode.equals(this.rightNode));
    }

    static class ThreadConcurrentTree implements Runnable {

        Tree[] nodes;
        int lowIdx;
        int highIdx;

        ThreadConcurrentTree(Tree[] nodes, int lowIdx, int highIdx) {
            this.nodes = nodes;
            this.lowIdx = lowIdx;
            this.highIdx = highIdx;
        }

        @Override
        public void run() {
            int leaf;
            for (int i = lowIdx; i < highIdx; i++) {
                leaf = 2 * i + 1;
                if (2 * i + 1 < nodes.length) {
                    nodes[i].setLeftNode(nodes[leaf++]);
                } else {
                    break;
                }
                if ((2 * (i + 1)) < nodes.length) {
                    nodes[i].setRightNode(nodes[leaf]);
                } else {
                    break;
                }

            }
        }
    }

    static class ThreadConcurrentRandomTree implements Runnable {

        Tree[] nodes;
        int lowIdx;
        int highIdx;

        ThreadConcurrentRandomTree(Tree[] nodes, int lowIdx, int highIdx) {
            this.nodes = nodes;
            this.lowIdx = lowIdx;
            this.highIdx = highIdx;
        }

        @Override
        public void run() {
            Tree currNode, tmpRoot;
            boolean left;
            Tree root = nodes[lowIdx];
            Random rnd = new Random();
            for (int i = lowIdx + 1; i < highIdx; i++) {
                tmpRoot = root;
                do {
                    currNode = tmpRoot;
                    if (rnd.nextBoolean()) {
                        tmpRoot = currNode.getLeftNode();
                        left = true;
                    } else {
                        tmpRoot = currNode.getRightNode();
                        left = false;
                    }

                } while (tmpRoot != null);
                if (left) {
                    currNode.setLeftNode(nodes[i]);
                } else {
                    currNode.setRightNode(nodes[i]);
                }

            }
        }
    }

}
