package consumerproducer.NoWaitNotify;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
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
