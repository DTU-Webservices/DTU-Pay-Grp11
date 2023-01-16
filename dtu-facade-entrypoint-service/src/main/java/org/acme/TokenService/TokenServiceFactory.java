package org.acme.TokenService;

import messaging.implementations.RabbitMqQueue;

/**
 * Inspiration drawn from Hubert Baumeiser 02267 Correlation Code Example
 *
 *
 */
public class TokenServiceFactory {

    static TokenService tokenService = null;
    public TokenService getTokenService() {
        return (tokenService != null) ? tokenService : getNewTokenService();
    }
    private TokenService getNewTokenService() {
        var mq = new RabbitMqQueue("rabbitMq");
        return new TokenService(mq);
    }
}
