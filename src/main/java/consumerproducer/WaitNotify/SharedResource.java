package consumerproducer.WaitNotify;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bertex
 */
public class SharedResource {

    private int resource;

    private boolean empty = true;

    public SharedResource() {
        this.resource = 0;
    }

    public synchronized void setValue(int resource) {
        while (!empty) {
            try {
              wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SharedResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.resource = resource;
        empty = false;
        notify();

    }

    public synchronized int getValue() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SharedResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        empty = true;
        notify();
        return this.resource;
    }

}
