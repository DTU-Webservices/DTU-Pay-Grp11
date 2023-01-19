package org.acme.Services;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entities.Customer;
import org.acme.Entities.Merchant;
import org.acme.Entities.Token;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Inspiration drawn from Hubert Baumeister 02267 Correlation Code Example
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke s191179.
 * @author Oliver Brink Klenum s193625.
 */


public class CustomerService {

    private static final String CUSTOMER_REGISTER_REQ = "CustomerAccRegisterReq";
    private static final String CUSTOMER_GET_REQ = "CustomerAccGetReq";
    private static final String CUSTOMER_DELETE_REQ = "CustomerAccDeleteReq";


    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Customer>> correlations = new ConcurrentHashMap<>();
    private final Map<CorrelationId, CompletableFuture<Token>> correlationsToken = new ConcurrentHashMap<>();

    public CustomerService(MessageQueue q) {
        queue = q;
        queue.addHandler("CustomerAccRegistered", this::handleCustomerRegister);
        queue.addHandler("CustomerAccGet", this::handleCustomerGet);
        queue.addHandler("CustomerAccDeleteResponse", this::handleCustomerDelete);
    }

    public Customer registerCustomer(Customer customer) {
        if(customer.getAccountId() == null) {
            return null;
        }
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId, new CompletableFuture<>());
        System.out.println("CustomerService: registerCustomer: " + customer);
        Event event = new Event(CUSTOMER_REGISTER_REQ, new Object[] { customer, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public boolean deleteCustomer(String customerId) {
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(CUSTOMER_DELETE_REQ, new Object[] { customerId, correlationId });
        queue.publish(event);
        return (correlations.get(correlationId).join() != null);
    }

    public Customer getCustomer(String customerId) {
        var correlationId = CorrelationId.randomId();
        Customer customer = new Customer();
        customer.setCustomerId(UUID.fromString(customerId));
        customer.setAccountId(null);
        correlations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(CUSTOMER_GET_REQ, new Object[] { customer, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public void handleCustomerRegister(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(customer);
    }

    public void handleCustomerDelete(Event ev) {
        Customer customer = ev.getArgument(0, Customer.class);
        CorrelationId correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(customer);
    }

    public void handleCustomerGet(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(customer);
    }
}
