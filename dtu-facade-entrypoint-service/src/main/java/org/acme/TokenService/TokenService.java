package org.acme.TokenService;

import messaging.CorrelationId;
import messaging.MessageQueue;
import messaging.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class TokenService {

    private static final String TOKEN_GENERATE_REQ = "TokenGenerateReq";
    private static final String TOKEN_GET_ACCOUNT_REQ = "TokenGetAccountReq";
    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Token>> pendingRequests = new ConcurrentHashMap<>();

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler("TokenGenerated", this::handleTokenGenerate);
        queue.addHandler("TokenGetAccount", this::handleTokenGetAccount);
    }

    public Token generateToken(Token token) {
        var correlationId = CorrelationId.randomId();
        pendingRequests.put(correlationId, new CompletableFuture<>());
        System.out.println("TokenService: generateToken: " + token);
        Event event = new Event(TOKEN_GENERATE_REQ, new Object[] {token, correlationId});
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }

    public Token getToken(String accountId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setAccountId(accountId);
        token.setTokenId(null);
        token.setTokens(null);
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKEN_GET_ACCOUNT_REQ, new Object[] {token, correlationId});
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }
    private void handleTokenGenerate(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }
    private void handleTokenGetAccount(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }
}
