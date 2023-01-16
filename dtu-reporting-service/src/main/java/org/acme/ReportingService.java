package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;

public class ReportingService {

    MessageQueue queue;

    public ReportingService(MessageQueue q) {
        queue = q;
    }

    public void handleAllPaymentsReportRequest(Event ev) {
        var correlationId =  ev.getArgument(0, CorrelationId.class);
    }

}
