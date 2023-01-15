package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.PaymentRepo;


public class PaymentService {

    private static final String PAYMENT_REQ = "PaymentRequested";
    private static final String AMOUNT_ASSIGNED = "AmountAssigned";
    private static final String CUSTOMER_REQUEST = "CustomerBankAccRequested";
    private static final String MERCHANT_REQUEST = "MerchantBankAccRequested";

    MessageQueue queue;

    public PaymentService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("PaymentCreateReq", this::handlePaymentRequested);
        //this.queue.addHandler(AMOUNT_ASSIGNED, this::handleAmountAssigned);
    }

    public void handlePaymentRequested(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        p.setPaymentId(correlationId.getId());
        PaymentRepo.addPayment(p);
        Event event = new Event(CUSTOMER_REQUEST, new Object[] { p, correlationId });
        queue.publish(event);
        Event event2 = new Event(MERCHANT_REQUEST, new Object[] { p, correlationId });
        queue.publish(event2);
    }

    public void handleAssigned(Event ev) {


    }

}
