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

    private static final String CUSTOMER_TOKENS_GENERATE = "CustomerTokensGenerate";

    private static final String CUSTOMER_TOKENS_AMOUNT_GET = "CustomerTokensAmountGet";


    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Customer>> correlations = new ConcurrentHashMap<>();

    private final Map<CorrelationId, CompletableFuture<Token>> tokenCorrelations = new ConcurrentHashMap<>();



    public CustomerService(MessageQueue q) {
        queue = q;
        queue.addHandler("CustomerAccRegistered", this::handleCustomerRegister);
        queue.addHandler("CustomerAccGet", this::handleCustomerGet);
        queue.addHandler("CustomerTokensGenerateReq", this::handleCustomerTokensGenerate);
        queue.addHandler("CustomerTokensAmountGetReq", this::handleCustomerTokensAmountGet);
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

    public Token generateCustomerTokens(Token token) {
        var correlationId = CorrelationId.randomId();
        tokenCorrelations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(CUSTOMER_TOKENS_GENERATE, new Object[] { token, correlationId });
        queue.publish(event);
        return tokenCorrelations.get(correlationId).join();
    }

    public Token getCustomerTokensAmount(String customerId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(customerId);
        token.setTokenId(null);
        token.setTokens(null);
        tokenCorrelations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(CUSTOMER_TOKENS_AMOUNT_GET, new Object[] { token, correlationId });
        queue.publish(event);
        return tokenCorrelations.get(correlationId).join();
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

    public void handleCustomerTokensGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        tokenCorrelations.get(correlationid).complete(token);
    }

    public void handleCustomerTokensAmountGet(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        tokenCorrelations.get(correlationid).complete(token);
    }
}
