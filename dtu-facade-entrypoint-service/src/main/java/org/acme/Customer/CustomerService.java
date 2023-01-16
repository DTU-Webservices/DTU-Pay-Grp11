package org.acme.Customer;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.TokenService.Token;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke s191179.
 */


public class CustomerService {

    private static final String CUSTOMER_REGISTER_REQ = "CustomerAccRegisterReq";
    private static final String CUSTOMER_GET_REQ = "CustomerAccGetReq";
    private static final String TOKENS_CUSTOMER_GENERATE_REQ = "TokensCustomerGenerateReq";

    private static final String TOKEN_CUSTOMER_GET_REQ = "TokenCustomerGetReq";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Customer>> correlations = new ConcurrentHashMap<>();

    public CustomerService(MessageQueue q) {
        queue = q;
        queue.addHandler("CustomerAccRegistered", this::handleCustomerRegister);
        queue.addHandler("CustomerAccGet", this::handleCustomerGet);
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

    public void generateCustomerTokens(Integer qty, String customerId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(customerId);
        token.setQty(qty);
        correlations.put(correlationId, new CompletableFuture<>());
        System.out.println("CustomerService: generateToken: " + qty + customerId);
        Event event = new Event(TOKENS_CUSTOMER_GENERATE_REQ, new Object[] {token , correlationId});
        queue.publish(event);
    }

    public void getCustomerToken(String customerId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(customerId);
        correlations.put(correlationId, new CompletableFuture<>());
        System.out.println("CustomerService: getToken: " + customerId);
        Event event = new Event(TOKEN_CUSTOMER_GET_REQ, new Object[] {token , correlationId});
        queue.publish(event);
    }

    public void handleCustomerRegister(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(customer);
    }

    public void handleCustomerGet(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(customer);
    }
}
