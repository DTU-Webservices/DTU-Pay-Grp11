package org.acme.Report;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.MoneyTransfer.MoneyTransfer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ReportService {

    private static final String REPORT_REQ = "ReportReq";

    private final MessageQueue queue;

    private final Map<CorrelationId, CompletableFuture<MoneyTransfer>> reportFuture = new ConcurrentHashMap<>();

    public ReportService(MessageQueue q) {
        queue = q;
        queue.addHandler("ReportCreated", this::handleReportCreated);
    }

    public void handleReportCreated(Event ev) {
        var rep = ev.getArgument(0, Report.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
    }

    /*public Report createReport(Report report) {
        var correlationId = new CorrelationId.randomId();

        reportFuture.put(correlationId, new CompletableFuture<>());
        Event event = new Event(REPORT_REQ, new Object[] { report });
        queue.publish(event);
        return reportFuture.get()
    }*/
}
