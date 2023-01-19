package org.acme;

import messaging.implementations.RabbitMqQueue;

/**
 * @author Kristoffer Torngaard Pedersen s205354
 */
public class Start {
    public static void main(String[] args) {
        new Start().start();
    }
    private void start() {
        var mq = new RabbitMqQueue("rabbitMq");
        new MerchantBankService(mq);
    }
}
