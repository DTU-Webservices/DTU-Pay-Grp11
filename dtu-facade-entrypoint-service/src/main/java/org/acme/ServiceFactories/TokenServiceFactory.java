package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.TokenService;

/**
 * Inspiration drawn from Hubert Baumeister 02267 Correlation Code Example
 *
 * @author Oliver Brink Klenum s193625.
 * @author Tobias Stærmose s205356.
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
