package consumerproducer.WaitNotify;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Puede ocurrir que los prints se desincronicen, sin embargo si se poner el valor i, el siguiente valor a sacar siempre sera i, aun cuando parezca antes que se metio el valor i+1
 * Esto es debido a que el hilo puede tener una demora a la hora de imprimir. Lo deje asi porque me parecio interesante.
 * @author bertex
 */
public class ProductorConsumidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        Consumidor  consumidor = new Consumidor(sharedResource);
        Productor productor =  new Productor(sharedResource);
        consumidor.start();
        productor.start();

    }

}
