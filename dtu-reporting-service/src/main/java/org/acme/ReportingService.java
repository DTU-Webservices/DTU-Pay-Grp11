package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.Report;

/**
 *
 * @author Oliver Brink Klenum s193625
 * @author Kristoffer Torngaard Pedersen
 * @author Tobias Stærmose
 *
 */
public class ReportingService {

    private static final String REPORT_ALL_PAYMENTS_RESP = "ReportAllPayResp";
    MessageQueue queue;

    public ReportingService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("ReportAllPay", this::handleAllPaymentsReport);
        this.queue.addHandler("ReportAllCustomerPay", this::handleAllPaymentsMadeByCustomerReport);
        this.queue.addHandler("ReportAllMerchantPay", this::handleAllPaymentsMadeByMerchantReport);
    }

    public void handleAllPaymentsReport(Event ev) {
        Report report = ev.getArgument(0, Report.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);
        Event event = new Event(REPORT_ALL_PAYMENTS_RESP, new Object[] {report, correlationId});
        queue.publish(event);
    }

    public void handleAllPaymentsMadeByCustomerReport(Event ev) {
        Report report = ev.getArgument(0, Report.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);
        Event event = new Event(REPORT_ALL_PAYMENTS_RESP, new Object[] {report, correlationId});
        queue.publish(event);
    }

    public void handleAllPaymentsMadeByMerchantReport(Event ev) {
        Report report = ev.getArgument(0, Report.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);
        Event event = new Event(REPORT_ALL_PAYMENTS_RESP, new Object[] {report, correlationId});
        queue.publish(event);
    }

}
