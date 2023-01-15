package org.acme;

import messaging.implementations.RabbitMqQueue;

public class Start {
    public static void main(String[] args) {
        new Start().start();
    }
    private void start() {
        var mq = new RabbitMqQueue("rabbitMq");
        new PaymentService(mq);
    }
}
