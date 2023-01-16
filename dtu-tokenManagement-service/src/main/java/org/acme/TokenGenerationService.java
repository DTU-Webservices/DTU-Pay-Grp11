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

    private static final String TOKENS_CUSTOMER_GENERATE = "TokensCustomerGenerate";
    private static final String TOKEN_GET_ACCOUNT = "TokenGetAccount";

    private static final String TOKENS_GET_ALL = "TokensGetAll";

    private static final String TOKEN_CUSTOMER_GET = "TokenCustomerGet";

    MessageQueue messageQueue;

    public TokenGenerationService(MessageQueue q) {
        this.messageQueue = q;
        this.messageQueue.addHandler("TokensCustomerGenerateReq", this::handleTokensCustomerGenerate);
        this.messageQueue.addHandler("TokenGetAccountReq", this::handleTokenGetAccount);
        this.messageQueue.addHandler("TokensGetAllReq", this::handleTokensGetAll);
        this.messageQueue.addHandler("TokenCustomerGetReq", this::handleTokenCustomerGet);
    }
    // Customer Gets {qty} tokens assigned to his account.
    private void handleTokensCustomerGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        // loop through qty and generate tokens
        for (int i = 0; i < token.getQty(); i++) {
            token.addToken(UUID.randomUUID().toString());
        }
        TokenRepo.addToken(token);
        Event event = new Event(TOKENS_CUSTOMER_GENERATE, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

    // Customer Gets all tokens assigned to his account.
    private void handleTokenGetAccount(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getToken(token.getCustomerId());
        Event event = new Event(TOKEN_GET_ACCOUNT, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }


    private void handleTokensGetAll(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getTokens();
        Event event = new Event(TOKENS_GET_ALL, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

    // Get one token to use for a payment
    private void handleTokenCustomerGet(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getToken(token.getTokens().get(0));
        Event event = new Event(TOKEN_CUSTOMER_GET, new Object[] {token, correlationId});
        messageQueue.publish(event);
        token.removeToken(token.getTokens().get(0));
    }
}
