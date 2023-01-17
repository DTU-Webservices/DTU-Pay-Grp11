package org.acme.Merchant;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke sxxxxxx.
 */


public class MerchantService {

    private static final String MERCHANT_REGISTER_REQ = "MerchantAccRegisterReq";
    private static final String MERCHANT_GET_REQ = "MerchantAccGetReq";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Merchant>> correlations = new ConcurrentHashMap<>();

    public MerchantService(MessageQueue q) {
        queue = q;
        queue.addHandler("MerchantAccRegistered", this::handleMerchantRegister);
        queue.addHandler("MerchantAccGet", this::handleMerchantGet);
    }

    public Merchant registerMerchant(Merchant merchant) {
        if(merchant.getAccountId() == null) {
            return null;
        }
        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId, new CompletableFuture<>());
        System.out.println("MerchantService: registerMerchant: " + merchant);
        Event event = new Event(MERCHANT_REGISTER_REQ, new Object[] { merchant, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public Merchant getMerchant(String merchantId) {
        var correlationId = CorrelationId.randomId();
        Merchant merchant = new Merchant();
        merchant.setMerchantId(UUID.fromString(merchantId));
        merchant.setAccountId(null);
        correlations.put(correlationId, new CompletableFuture<>());
        Event event = new Event(MERCHANT_GET_REQ, new Object[] { merchant, correlationId });
        queue.publish(event);
        return correlations.get(correlationId).join();
    }

    public void handleMerchantRegister(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        System.out.println(correlations);
        System.out.println(correlations.get(correlationid));
        correlations.get(correlationid).complete(merchant);
    }

    public void handleMerchantGet(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationid = ev.getArgument(1, CorrelationId.class);
        correlations.get(correlationid).complete(merchant);
    }

}