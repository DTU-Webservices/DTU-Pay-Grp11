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
    private static final String TOKEN_GET_ACCOUNT_REQ = "TokenGetAccountReq";

    private static final String TOKENS_GET_ALL_REQ = "TokensGetAllReq";
    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Token>> pendingRequests = new ConcurrentHashMap<>();

    public TokenService(MessageQueue q) {
        queue = q;
        queue.addHandler("TokensGet", this::handleTokensGet);
        queue.addHandler("TokenGetAccount", this::handleTokenGetAccount);
        queue.addHandler("TokensGetAll", this::handleTokensGetAll);
        queue.addHandler("TokenCustomerGet", this::handleTokenCustomerGet);
    }


    public Token getToken(String accountId) {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(accountId);
        token.setTokenId(null);
        token.setTokens(null);
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKEN_GET_ACCOUNT_REQ, new Object[] {token, correlationId});
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }

    public Token getTokens() {
        var correlationId = CorrelationId.randomId();
        Token token = new Token();
        token.setCustomerId(null);
        token.setTokenId(null);
        token.setTokens(null);
        pendingRequests.put(correlationId, new CompletableFuture<>());
        Event event = new Event(TOKENS_GET_ALL_REQ, new Object[] {token, correlationId});
        queue.publish(event);
        return pendingRequests.get(correlationId).join();
    }
    private void handleTokensGet(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }
    private void handleTokenGetAccount(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }

    private void handleTokensGetAll(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }

    private void handleTokenCustomerGet(Event event) {
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        pendingRequests.get(correlationId).complete(token);
    }


}
