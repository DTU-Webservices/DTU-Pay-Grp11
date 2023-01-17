package org.acme.Merchant;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.MoneyTransfer.MoneyTransfer;
import org.acme.MoneyTransfer.Payment;

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
    private static final String MERCHANT_PAYMENT_CREATE = "MerchantPaymentCreate";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Merchant>> correlations = new ConcurrentHashMap<>();
    private final Map<CorrelationId, CompletableFuture<MoneyTransfer>> correlationsPayment = new ConcurrentHashMap<>();

    public MerchantService(MessageQueue q) {
        queue = q;
        queue.addHandler("MerchantAccRegistered", this::handleMerchantRegister);
        queue.addHandler("MerchantAccGet", this::handleMerchantGet);
        queue.addHandler("PaymentCreated", this::handleMerchantPaymentCreate);
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

    public MoneyTransfer createPayment (Payment payment) {
        var correlationId = CorrelationId.randomId();
        payment.setPaymentId(correlationId.getId());
        System.out.println("MerchantService: create payment: " + payment);
        correlationsPayment.put(correlationId, new CompletableFuture<>());
        Event event = new Event(MERCHANT_PAYMENT_CREATE, new Object[] {payment, correlationId});
        queue.publish(event);
        return correlationsPayment.get(correlationId).join();
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

    public void handleMerchantPaymentCreate(Event ev) {
        var mt = ev.getArgument(0, MoneyTransfer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        try {
            correlationsPayment.get(correlationId).complete(mt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}