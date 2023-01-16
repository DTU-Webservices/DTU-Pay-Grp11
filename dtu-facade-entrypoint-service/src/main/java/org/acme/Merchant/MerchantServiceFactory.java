package org.acme.Merchant;

import messaging.implementations.RabbitMqQueue;

/**
 * Inspiration drawn from Hubert Baumeiser 02267 Correlation Code Example
 *
 * @author Kristoffer T. Pedersen s205354.
 */
public class MerchantServiceFactory {

    static MerchantService service = null;
    public MerchantService getMerchantService() {
        return (service != null) ? service : getNewMerchantService();
    }
    private MerchantService getNewMerchantService() {
        var mq = new RabbitMqQueue("rabbitMq");
        return new MerchantService(mq);
    }

}
