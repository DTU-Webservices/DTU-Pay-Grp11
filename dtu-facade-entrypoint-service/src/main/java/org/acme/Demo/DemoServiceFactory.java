package org.acme.Demo;

import messaging.implementations.RabbitMqQueue;

/**
 * Inspiration drawn from Hubert Baumeiser 02267 Correlation Code Example
 *
 * @author Kristoffer T. Pedersen s205354.
 */
public class DemoServiceFactory {

    static DemoService service = null;
    public DemoService getDemoService() {
        return (service != null) ? service : getNewDemoService();
    }
    private DemoService getNewDemoService() {
        var mq = new RabbitMqQueue("rabbitMq");
        return new DemoService(mq);
    }

}
