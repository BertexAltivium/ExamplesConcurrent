package consumerproducer.WaitNotify;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bertex
 */
public class Consumidor extends Thread{

    private final SharedResource resource;

    private final int MIN_TIME = 100;
    private final int MAX_TIME = 300;

    public Consumidor(SharedResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        int value;
        while (true) {

            value  = resource.getValue();
            System.out.println("Obtuve el valor" + value);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(MIN_TIME, MAX_TIME));
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }



}
