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

    private static final String TOKENS_GENERATED = "TokensGenerated";
    private static final String TOKEN_GET_ACCOUNT = "TokenGetAccount";

    private static final String TOKENS_GET_TOKEN = "TokensGetToken";

    MessageQueue messageQueue;

    public TokenGenerationService(MessageQueue q) {
        this.messageQueue = q;
        this.messageQueue.addHandler("TokensGenerateReq", this::handleTokensGenerate);
        this.messageQueue.addHandler("TokenGetAccountReq", this::handleTokenGetAccount);
        this.messageQueue.addHandler("TokensGetTokenReq", this::handleTokensGetToken);
    }
    // Customer Gets {qty} tokens assigned to his account.
    private void handleTokensGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        // loop through qty and generate tokens
        int tokenQty = Integer.parseInt(token.getQty());
        if (tokenQty <= 6) {
            for (int i = 0; i < tokenQty; i++) {
                token.addToken(UUID.randomUUID().toString());
            }
        }
        //TODO: Can only generate tokens if customer has 0 or 1 token left
        //TODO: If customer has 1 token left he can max generate 5 tokens
        //TODO: If customer has 0 tokens left he can max generate 6 tokens
        TokenRepo.addToken(token);
        Event event = new Event(TOKENS_GENERATED, new Object[] {token, correlationId});
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

    // Customer Gets a specific token assigned to his account.
    private void handleTokensGetToken(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getToken(token.getCustomerId());
        Event event = new Event(TOKENS_GET_TOKEN, new Object[] {token, correlationId});
        messageQueue.publish(event);
        token.removeToken(token.getTokens().get(0));
    }
}
