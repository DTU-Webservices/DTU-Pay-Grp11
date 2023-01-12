package tokengeneration.service;

import messaging.implementations.RabbitMqQueue;

public class StartUp {
    public static void main(String[] args) throws Exception {
        new StartUp().run();
    }

    public void run() throws Exception {
        var mq = new RabbitMqQueue("rabbitMq");
        new TokenIdService(mq);
    }
}