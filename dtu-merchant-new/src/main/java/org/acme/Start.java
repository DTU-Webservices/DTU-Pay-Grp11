package org.acme;

import messaging.implementations.RabbitMqQueue;
import org.acme.DemoRec.DemoRecService;

public class Start {
    public static void main(String[] args) {
        new Start().start();
    }
    private void start() {
        var mq = new RabbitMqQueue("rabbitMq");
        new DemoRecService(mq);
    }
}
