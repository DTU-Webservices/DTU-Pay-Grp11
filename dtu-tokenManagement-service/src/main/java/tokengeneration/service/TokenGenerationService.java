package tokengeneration.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import messaging.MessageQueue;
import messaging.Event;


public class TokenGenerationService {
    public static final String TOKEN_GENERATION_REQUESTED = "TokenGenerationRequested";
    public static final String CUSTOMER_TOKEN_ASSIGNED = "CustomerTokenAssigned";
    private MessageQueue queue;
    private Map<CorrelationId, CompletableFuture<Token>> correlations = new ConcurrentHashMap<>();

    public TokenGenerationService(MessageQueue q) {
        queue = q;
        queue.addHandler(CUSTOMER_TOKEN_ASSIGNED, this::handleCustomerTokenAssigned);
    }

    public Token generateToken(Token t) {
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId,new CompletableFuture<>());
        Event event = new Event(TOKEN_GENERATION_REQUESTED, new Object[] { t, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public void handleCustomerTokenAssigned(Event e) {
        var t = e.getArgument(0, Token.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(t);
    }
}
