package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.Customer;
import org.acme.Entity.Payment;
import org.acme.Entity.Token;
import org.acme.Repo.TokenRepo;

import java.util.UUID;

/**
 *
 * @author Oliver Brink Klenum s193625
 * @author Tobias Stærmose s205356
 * @author Kristoffer T. Pedersen s205354
 *
 */

public class TokenService {

    private static final String TOKENS_GENERATED = "TokensGenerated";
    private static final String TOKENS_GET_TOKEN = "TokensGetToken";
    private static final String TOKENS_AMOUNT_GET = "TokensAmountGet";
    private static final String GET_CUSTOMER_ID_FROM_TOKEN_RES = "GetCustomerIdFromTokenRes";

    MessageQueue messageQueue;

    public TokenService(MessageQueue q) {
        this.messageQueue = q;
        this.messageQueue.addHandler("TokensGenerateReq", this::handleTokensGenerate);
        this.messageQueue.addHandler("TokensGetTokenReq", this::handleTokensGetToken);
        this.messageQueue.addHandler("TokensAmountGetReq", this::handleTokensAmountGet);
        this.messageQueue.addHandler("GetCustomerIdFromTokenReq", this::handleGetCustomerIdFromToken);
    }
    // Customer Gets {qty} tokens assigned to his account.
    public void handleTokensGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        var tokenOut = addTokensToTokenList(token);

        Event event = new Event(TOKENS_GENERATED, new Object[] {tokenOut, correlationId});
        messageQueue.publish(event);
    }

    private Token addTokensToTokenList(Token token) {
        int tokenQty = Integer.parseInt(token.getQty());
        if (TokenRepo.getToken(token.getCustomerId()) != null) {
            if (TokenRepo.getNumberOfTokens(token.getCustomerId()) == 0 && tokenQty <= 6) {
                for (int i = 0; i < tokenQty; i++) {
                    token.addToken(UUID.randomUUID());
                }
                TokenRepo.addToken(token);
                token.setQty(Integer.toString(tokenQty));
            } else if (TokenRepo.getNumberOfTokens(token.getCustomerId()) == 1 && tokenQty <= 5) {

                for (int i = 0; i < tokenQty; i++) {
                    token.addToken(UUID.randomUUID());
                }
                TokenRepo.addToken(token);
                token.setQty(Integer.toString(tokenQty));
            } else {
                token.setQty("0");

            }
        } else {
            if (tokenQty <= 6) {
                for (int i = 0; i < tokenQty; i++) {
                    token.addToken(UUID.randomUUID());
                }
                TokenRepo.addToken(token);
                token.setQty(Integer.toString(tokenQty));
            } else {
                token.setQty("0");
            }
        }
        return token;
    }
    // Customer Gets a specific token assigned to his account.
    public void handleTokensGetToken(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        var tokenOut = checkIfTokenExistsInList(token);
        Event event = new Event(TOKENS_GET_TOKEN, new Object[] {tokenOut, correlationId});
        messageQueue.publish(event);
    }

    private Token checkIfTokenExistsInList(Token token) {
        token = TokenRepo.getToken(token.getCustomerId());
        if (token.getTokens().isEmpty()) {
            token.setCurrentToken("No tokens available");
        } else {
            token.setCurrentToken(token.getTokens().get(0).toString());
        }
        return token;
    }

    public void handleTokensAmountGet(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getToken(token.getCustomerId());
        Event event = new Event(TOKENS_AMOUNT_GET, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

    public void handleGetCustomerIdFromToken(Event ev) {
        Payment payment = ev.getArgument(0, Payment.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);
        Customer customer = new Customer();
        UUID customerId = TokenRepo.getCidToken(UUID.fromString(payment.getToken()));
        customer.setCustomerId(customerId);
        customer.setCurrentToken(UUID.fromString(payment.getToken()));
        TokenRepo.getToken(customerId).removeToken(UUID.fromString(payment.getToken()));

        Event event = new Event(GET_CUSTOMER_ID_FROM_TOKEN_RES, new Object[] {customer, correlationId});
        messageQueue.publish(event);
    }


}
