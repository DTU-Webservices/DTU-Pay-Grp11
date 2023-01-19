package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.CustomerService;


/**
 * Inspiration drawn from Hubert Baumeister 02267 Correlation Code Example
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke s191179.
 */
public class CustomerServiceFactory {

    static CustomerService service = null;
    public CustomerService getCustomerService() {
        return (service != null) ? service : getNewCustomerService();
    }
    private CustomerService getNewCustomerService() {
        var mq = new RabbitMqQueue("rabbitMq");
        return new CustomerService(mq);
    }

}