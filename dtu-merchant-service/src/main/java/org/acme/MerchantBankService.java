package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.MerchantRepo;


public class MerchantBankService {

    private static final String MERCHANT_ACC_REGISTER = "MerchantAccRegistered";
    private static final String MERCHANT_GET_ACCOUNT = "MerchantAccGet";

    private static final String MERCHANT_ACCOUNT_RESPONSE = "MerchantAccResponse";

    MessageQueue queue;

    public MerchantBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("MerchantAccRegisterReq", this::handleMerchantAccountRegister);
        this.queue.addHandler("MerchantAccGetReq", this::handleMerchantAccountGet);
        this.queue.addHandler("GetMerchantAccForTransferReq", this::handleMerchantAccountGetForTransfer);
    }

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

    public void handleMerchantAccountGetForTransfer(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        merchant = MerchantRepo.getMerchant(merchant.getMerchantId());
        Event event = new Event(MERCHANT_ACCOUNT_RESPONSE, new Object[] { merchant, correlationId });
        queue.publish(event);
    }
}