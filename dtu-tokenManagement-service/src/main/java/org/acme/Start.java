package org.acme;

import messaging.implementations.RabbitMqQueue;


public class Start {

    private void start() {
        var mq = new RabbitMqQueue("rabbitMq");
        new TokenService(mq);
    }

    public static void main(String[] args) {
        new Start().start();
    }
}
