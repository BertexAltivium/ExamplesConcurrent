package consumerproducer.NoWaitNotify;/*
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
public class SharedResource {

    private int resource;

    private static boolean empty = true;

    private final int MIN_TIME = 10;
    private final int MAX_TIME = 30;

    public SharedResource() {
        this.resource = 0;
    }

    public  void setValue(int resource) {

        while (!empty) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(MIN_TIME, MAX_TIME));
            } catch (InterruptedException ex) {
                Logger.getLogger(SharedResource.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        this.resource = resource;
        empty = false;
    }

    public  int getValue() {
        while (empty) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(MIN_TIME, MAX_TIME));
            } catch (InterruptedException ex) {
                Logger.getLogger(SharedResource.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        empty = true;

        return this.resource;
    }


}
