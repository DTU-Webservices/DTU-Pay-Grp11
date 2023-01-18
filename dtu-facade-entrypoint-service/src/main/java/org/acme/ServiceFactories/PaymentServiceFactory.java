package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.PaymentService;

/**
 * @Author Kristoffer T. Pedersen, Lauritz Pepke
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
