package org.acme.TokenService;

import messaging.CorrelationId;
import messaging.MessageQueue;
import messaging.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 *
 * @author Oliver Brink Klenum s193625
 * @author Tobias Stærmose sxxxxxx
 */

public class TokenService {

    private static final String TOKENS_GENERATE_REQ = "TokensGenerateReq";

    private static final String TOKEN_GET_ACCOUNT_REQ = "TokenGetAccountReq";

    private static final String TOKENS_GET_TOKEN_REQ = "TokensGetTokenReq";


    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Token>> pendingRequests = new ConcurrentHashMap<>();

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler("TokensGenerated", this::handleTokensGenerated);
        queue.addHandler("TokenGetAccount", this::handleTokenGetAccount);
        queue.addHandler("TokensGetToken", this::handleTokensGetToken);
    }

    public Token generateTokens(Token token) {
        var correlationId = CorrelationId.randomId();
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKENS_GENERATE_REQ, new Object[] { token, correlationId });
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }
    public Token getTokenByCustomerId(String customerId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(customerId);
        token.setTokenId(null);
        token.setTokens(null);
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKEN_GET_ACCOUNT_REQ, new Object[] {token, correlationId});
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }

    public Token getTokenForPayment(String customerId) {
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

    private void handleTokensGenerated(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }

    private void handleTokenGetAccount(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }

    private void handleTokensGetToken(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }
}
