package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.MerchantRepo;

public class MerchantBankService {

    private static final String MERCHANT_ACC_REGISTER = "MerchantAccRegistered";
    private static final String MERCHANT_GET_ACCOUNT = "MerchantAccGet";

    MessageQueue queue;

    public MerchantBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("MerchantAccRegisterReq", this::handleMerchantAccountRegister);
        //this.queue.addHandler("AmountRequested", this::handleAmountRequested);
        this.queue.addHandler("MerchantAccGetReq", this::handleMerchantAccountGet);
    }

//    public void handleAmountRequested(Event ev) {
//        var p = ev.getArgument(0, Payment.class);
//        //temporary value
//        p.setAmount("10");
//        Event event = new Event("AmountAssigned", new Object[] { p });
//        queue.publish(event);
//    }

    public void handleMerchantAccountRegister(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        merchant.setMerchantId(correlationId.getId());
        MerchantRepo.addMerchant(merchant);
        Event event = new Event(MERCHANT_ACC_REGISTER, new Object[] { merchant, correlationId });
        queue.publish(event);
    }

    public void handleMerchantAccountGet(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        merchant = MerchantRepo.getMerchant(merchant.getMerchantId());
        Event event = new Event(MERCHANT_GET_ACCOUNT, new Object[] { merchant, correlationId });
        queue.publish(event);
    }
}
