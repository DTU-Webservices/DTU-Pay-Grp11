package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.TokenService;

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
