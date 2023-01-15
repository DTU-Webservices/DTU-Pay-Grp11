package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.MoneyTransferRepo;
import org.acme.Repo.PaymentRepo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {

    private static final String PAYMENT_CREATED = "PaymentCreated";
    private static final String AMOUNT_ASSIGNED = "AmountAssigned";
    private static final String CUSTOMER_REQUEST = "GetCustomerAccForTransferReq";
    private static final String MERCHANT_REQUEST = "GetMerchantAccForTransferReq";

    MessageQueue queue;

    public PaymentService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("PaymentCreateReq", this::handlePaymentRequested);
        this.queue.addHandler("MerchantAccResponse", this::handleMerchantAccountIdGetReq);
        this.queue.addHandler("CustomerAccResponse", this::handleCustomerAccountIdGetReq);
    }
    public void handlePaymentRequested(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        p.setPaymentId(correlationId.getId());
        PaymentRepo.addPayment(p);

        var merchant = new Merchant();
        merchant.setMerchantId(UUID.fromString(p.getMid()));
        Event event = new Event(MERCHANT_REQUEST, new Object[] {merchant, correlationId });
        queue.publish(event);
    }

    public void handleMerchantAccountIdGetReq(Event ev) {
        var merchant = ev.getArgument(0, Merchant.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);

        var p = PaymentRepo.getPayment(correlationId.getId());

        var mt = new MoneyTransfer();
        mt.setMtId(correlationId.getId());
        mt.setAmount(p.getAmount());
        mt.setMAccountId(merchant.getAccountId());

        MoneyTransferRepo.addMoneyTransfer(mt);

        var customer = new Customer();
        customer.setCustomerId(UUID.fromString(p.getCid()));

        Event event = new Event(CUSTOMER_REQUEST, new Object[] {customer, correlationId});
        queue.publish(event);
    }

    public void handleCustomerAccountIdGetReq(Event ev){
        var customer = ev.getArgument(0, Customer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);

        var mt = MoneyTransferRepo.getMoneyTransfer(correlationId.getId());

        mt.setCAccountId(customer.getAccountId());

        //Kode til at lave bank overførsel her eller noget.

        System.out.println("THIS IS NOISE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Event event = new Event(PAYMENT_CREATED, new Object[] {mt, correlationId});
        queue.publish(event);
    };


}
