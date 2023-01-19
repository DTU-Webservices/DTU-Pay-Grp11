package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.PaymentService;

/**
 * Inspiration drawn from Hubert Baumeister 02267 Correlation Code Example
 *
 * @author Kristoffer T. Pedersen s205354.
 */

public class PaymentServiceFactory {

    static PaymentService service = null;
    public PaymentService getPaymentService() {
        return (service != null) ? service : getNewPaymentService();
    }
    private PaymentService getNewPaymentService() {
        var mq = new RabbitMqQueue("rabbitMq");
        return new PaymentService(mq);
    }
}
