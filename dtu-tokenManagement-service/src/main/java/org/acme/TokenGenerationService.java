package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.TokenRepo;

import java.util.UUID;

/**
 *
 * @author Oliver Brink Klenum s193625
 * @author Tobias Stærmose sxxxxxx
 *
 */

public class TokenGenerationService {

    private static final String TOKEN_GENERATE = "TokenGenerated";
    private static final String TOKEN_GET_ACCOUNT = "TokenGetAccount";

    MessageQueue messageQueue;

    public TokenGenerationService(MessageQueue q) {
        this.messageQueue = q;
        this.messageQueue.addHandler("TokenGenerateReq", this::handleTokenGenerate);
        this.messageQueue.addHandler("TokenGetAccountReq", this::handleTokenGetAccount);
    }

    private void handleTokenGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        int maxTokens = 6;
        int currentTokens = TokenRepo.getTokensAmount(token.getAccountId());
        System.out.println("Current tokens: " + currentTokens);
        // loop through qty and generate tokens
        for (int i = 0; i < token.getQty(); i++) {
            token.addToken(UUID.randomUUID().toString());
        }
        TokenRepo.addToken(token);
        Event event = new Event(TOKEN_GENERATE, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

/*
    private void handleTokenGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        // loop through qty and generate tokens
        for (int i = 0; i < token.getQty(); i++) {
            token.addToken(UUID.randomUUID().toString());
        }
        TokenRepo.addToken(token);
        Event event = new Event(TOKEN_GENERATE, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }
*/
    private void handleTokenGetAccount(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getToken(token.getAccountId());
        Event event = new Event(TOKEN_GET_ACCOUNT, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }
}
