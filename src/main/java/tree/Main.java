package tree;/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */

/**
 *
 * @author bertex
 */
public class Main {

    public static void main(String[] args) {
        Tree[] nodes = Tree.initArray();
        Tree[] nodes2 = Tree.initArray();
        Tree[] randomeNodes = Tree.initArray();
        Tree[] randomeConcurrentNodes = Tree.initArray();

        long startTime = System.currentTimeMillis();
        Tree tree2 = Tree.createTree(nodes2);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tiempo secuencial determinista: " + elapsedTime);

        startTime = System.currentTimeMillis();
        Tree root = Tree.createConcurrentTree(nodes);
        stopTime = System.currentTimeMillis();
        long elapsedTimeConcurrent = stopTime - startTime;
        System.out.println("Tiempo concurrente determinista: " + elapsedTimeConcurrent);

        System.out.println(root.equals(tree2));
        System.out.println("Speedup determinista: " + ((double) elapsedTime / elapsedTimeConcurrent));

        startTime = System.currentTimeMillis();
        Tree random = Tree.randomTree(randomeNodes);
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        System.out.println("Tiempo secuencial aleatorio: " + elapsedTime);

        startTime = System.currentTimeMillis();
        Tree randomConcurrent = Tree.concurrentRandomTree(randomeConcurrentNodes);
        stopTime = System.currentTimeMillis();
        elapsedTimeConcurrent = stopTime - startTime;
        System.out.println("Tiempo concurrente aleatorio: " + elapsedTimeConcurrent);

        System.out.println("Speedup aleatorio: " + ((double) elapsedTime / elapsedTimeConcurrent));
    }
}
