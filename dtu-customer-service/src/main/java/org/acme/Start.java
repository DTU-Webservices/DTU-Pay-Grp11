package org.acme;

import messaging.implementations.RabbitMqQueue;

/**
 * @author Lauritz Pepke s191179.
 */

public class Start {
    public static void main(String[] args) {
        new Start().start();
    }
    private void start() {
        var mq = new RabbitMqQueue("rabbitMq");
        new CustomerBankService(mq);
    }
}