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
 *
 */

public class TokenService {

    private static final String TOKENS_GENERATED = "TokensGenerated";
    private static final String TOKEN_GET_ACCOUNT = "TokenGetAccount";
    private static final String TOKENS_GET_TOKEN = "TokensGetToken";
    private static final String CUSTOMER_TOKENS_GENERATE_REQ = "CustomerTokensGenerateReq";
    private static final String CUSTOMER_TOKENS_AMOUNT_GET_REQ = "CustomerTokensAmountGetReq";
    private static final String GET_CUSTOMER_ID_FROM_TOKEN_RES = "GetCustomerIdFromTokenRes";

    MessageQueue messageQueue;

    public TokenService(MessageQueue q) {
        this.messageQueue = q;
        this.messageQueue.addHandler("TokensGenerateReq", this::handleTokensGenerate);
        this.messageQueue.addHandler("TokenGetAccountReq", this::handleTokenGetAccount);
        this.messageQueue.addHandler("TokensGetTokenReq", this::handleTokensGetToken);
        this.messageQueue.addHandler("CustomerTokensGenerate", this::handleCustomerTokensGenerate);
        this.messageQueue.addHandler("CustomerTokensAmountGet", this::handleCustomerTokensAmountGet);
        this.messageQueue.addHandler("GetCustomerIdFromTokenReq", this::handleGetCustomerIdFromToken);
    }
    // Customer Gets {qty} tokens assigned to his account.
    public void handleTokensGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        addTokensToTokenList(token);

        Event event = new Event(TOKENS_GENERATED, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

    private void addTokensToTokenList(Token token) {
        int tokenQty = Integer.parseInt(token.getQty());
        int maxTokens;
        if (TokenRepo.getToken(token.getCustomerId()) != null) {
            maxTokens = (TokenRepo.getNumberOfTokens(token.getCustomerId()) == 0) ? 6 : 5;
        } else {
            maxTokens = 6;
        }
        if (tokenQty <= maxTokens) {
            for (int i = 0; i < tokenQty; i++) {
                token.addToken(UUID.randomUUID());
            }
            TokenRepo.addToken(token);
        }
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

    }

    private void handleCustomerTokensGenerate(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token.setTokenId(correlationId.getId());
        // loop through qty and generate tokens
        int tokenQty = Integer.parseInt(token.getQty());
        if (TokenRepo.getToken(token.getCustomerId()) != null) {
            if (TokenRepo.getNumberOfTokens(token.getCustomerId()) == 0 && tokenQty <= 6) {
                for (int i = 0; i < tokenQty; i++) {
                    token.addToken(UUID.randomUUID());
                }
                TokenRepo.addToken(token);
            } else if (TokenRepo.getNumberOfTokens(token.getCustomerId()) == 1 && tokenQty <= 5) {

                for (int i = 0; i < tokenQty; i++) {
                    token.addToken(UUID.randomUUID());
                }
                TokenRepo.addToken(token);
            }
        } else {
            if (tokenQty <=6) {
                for (int i = 0; i < tokenQty; i++) {
                    token.addToken(UUID.randomUUID());
                }
                TokenRepo.addToken(token);
            }

        }
        Event event = new Event(CUSTOMER_TOKENS_GENERATE_REQ, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

    private void handleCustomerTokensAmountGet(Event ev) {
        var token = ev.getArgument(0, Token.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        token = TokenRepo.getToken(token.getCustomerId());
        Event event = new Event(CUSTOMER_TOKENS_AMOUNT_GET_REQ, new Object[] {token, correlationId});
        messageQueue.publish(event);
    }

    private void handleGetCustomerIdFromToken(Event ev) {
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
