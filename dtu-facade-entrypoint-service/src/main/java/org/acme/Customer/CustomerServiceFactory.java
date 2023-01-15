package org.acme.Customer;

import messaging.implementations.RabbitMqQueue;


/**
 * Inspiration drawn from Hubert Baumeiser 02267 Correlation Code Example
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