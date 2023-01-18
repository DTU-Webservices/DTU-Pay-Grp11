package org.acme.Services;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entities.Report;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ReportService {

    private static final String REPORT_ALL_PAYMENTS_REQ = "ReportAllPayReq";
    private static final String REPORT_ALL_CUSTOMER_PAYMENTS_REQ = "ReportAllCustomerPayReq";
    private static final String REPORT_ALL_MERCHANT_PAYMENTS_INVOLVED_REQ = "ReportAllMerchantPayReq";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<Report>> reportFuture = new ConcurrentHashMap<>();

    public ReportService(MessageQueue q) {
        queue = q;
        queue.addHandler("ReportAllPayResp", this::handleAllPaymentsReportResponse);
    }

    public Report getAllPayments(){
        return sendRequestForReportAndReturnResponse(REPORT_ALL_PAYMENTS_REQ);
    }

    public Report getAllPaymentsMadeByCustomer(){
        return sendRequestForReportAndReturnResponse(REPORT_ALL_CUSTOMER_PAYMENTS_REQ);
    }

    public Report getAllPaymentsWithMerchantInvolved(){
        return sendRequestForReportAndReturnResponse(REPORT_ALL_MERCHANT_PAYMENTS_INVOLVED_REQ);
    }

    private Report sendRequestForReportAndReturnResponse(String requestHandler) {
        var correlationId = CorrelationId.randomId();
        reportFuture.put(correlationId, new CompletableFuture<>());
        Event event = new Event(requestHandler, new Object[] {correlationId});
        queue.publish(event);
        return reportFuture.get(correlationId).join();
    }

    private void handleAllPaymentsReportResponse(Event event) {
        var report = event.getArgument(0, Report.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        try {
            reportFuture.get(correlationId).complete(report);
        } catch (Exception e) {
            System.out.println(report);
            e.printStackTrace();
        }

    }
}
