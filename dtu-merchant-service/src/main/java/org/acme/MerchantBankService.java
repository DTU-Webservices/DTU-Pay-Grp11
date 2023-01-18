package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.MerchantRepo;

import java.util.UUID;


public class MerchantBankService {

    private static final String MERCHANT_ACC_REGISTER = "MerchantAccRegistered";
    private static final String MERCHANT_GET_ACCOUNT = "MerchantAccGet";
    private static final String MERCHANT_ACCOUNT_RESPONSE = "MerchantAccResponse";
    private static final String MERCHANT_DELETE_RESPONSE = "MerchantAccDeleteResponse";
    private static final String MERCHANT_ID_GET_RESPONSE = "MerchantIdGetResponse";

    MessageQueue queue;

    public MerchantBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("MerchantAccRegisterReq", this::handleMerchantAccountRegister);
        this.queue.addHandler("MerchantAccGetReq", this::handleMerchantAccountGet);
        this.queue.addHandler("GetMerchantAccForTransferReq", this::handleMerchantAccountGetForTransfer);
        this.queue.addHandler("ReportAllMerchantPayReq", this::handleReportAllMerchantPaymentsRequest);
        this.queue.addHandler("MerchantAccDeleteReq", this::handleMerchantAccountDelete);
    }

    public void handleMerchantAccountRegister(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        merchant.setMerchantId(correlationId.getId());
        MerchantRepo.addMerchant(merchant);
        Event event = new Event(MERCHANT_ACC_REGISTER, new Object[] { merchant, correlationId });
        queue.publish(event);
    }

    public void handleReportAllMerchantPaymentsRequest(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        merchant.setMerchantId(merchant.getMerchantId());
        merchant = MerchantRepo.getMerchant(merchant.getMerchantId());
        Event event = new Event(MERCHANT_ID_GET_RESPONSE, new Object[] { merchant, correlationId });
        queue.publish(event);
    }

    public void handleMerchantAccountGet(Event ev) {
        handleMerchantAccountRequestsForDifferentQueues(MERCHANT_GET_ACCOUNT, ev);
    }

    public void handleMerchantAccountGetForTransfer(Event ev) {
        handleMerchantAccountRequestsForDifferentQueues(MERCHANT_ACCOUNT_RESPONSE, ev);
    }

    public void handleMerchantAccountRequestsForDifferentQueues(String responseHandler, Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        merchant = MerchantRepo.getMerchant(merchant.getMerchantId());
        Event event = new Event(responseHandler, new Object[] { merchant, correlationId });
        queue.publish(event);
    }

    public void handleMerchantAccountDelete(Event ev) {
        var merchantId = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        var merchant = MerchantRepo.getMerchant(UUID.fromString(merchantId));
        MerchantRepo.deleteMerchant(UUID.fromString(merchantId));
        Event event = new Event(MERCHANT_DELETE_RESPONSE, new Object[] { merchant, correlationId });
        queue.publish(event);
    }
}