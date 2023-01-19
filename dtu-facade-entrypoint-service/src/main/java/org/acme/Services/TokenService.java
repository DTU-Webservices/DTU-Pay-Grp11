package org.acme.Services;

import messaging.CorrelationId;
import messaging.MessageQueue;
import messaging.Event;
import org.acme.Entities.Token;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Inspiration drawn from Hubert Baumeister 02267 Correlation Code Example
 *
 * @author Oliver Brink Klenum s193625.
 * @author Kristoffer Torngaard Pedersen s205354
 * @author Tobias Stærmose s205356
 *
 */

public class TokenService {

    private static final String TOKENS_GENERATE_REQ = "TokensGenerateReq";

    private static final String TOKENS_GET_TOKEN_REQ = "TokensGetTokenReq";

    private static final String TOKENS_AMOUNT_GET_REQ = "TokensAmountGetReq";


    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Token>> pendingRequests = new ConcurrentHashMap<>();

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler("TokensGenerated", this::handleTokensGenerated);
        queue.addHandler("TokensGetToken", this::handleTokensGetToken);
        queue.addHandler("TokensAmountGet", this::handleTokensAmountGet);
    }

    public Token generateTokens(Token token) {
        var correlationId = CorrelationId.randomId();
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKENS_GENERATE_REQ, new Object[] { token, correlationId });
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }

    public Token getTokenForPayment(UUID customerId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(customerId);
        token.setTokenId(null);
        token.setTokens(null);
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKENS_GET_TOKEN_REQ, new Object[] {token, correlationId});
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }

    public Token getTokensAmount(UUID customerId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(customerId);
        token.setTokenId(null);
        token.setTokens(null);
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKENS_AMOUNT_GET_REQ, new Object[] { token, correlationId });
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }

    private void handleTokensGenerated(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }

    private void handleTokensGetToken(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }

    public void handleTokensAmountGet(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationid).complete(token);
    }
}
