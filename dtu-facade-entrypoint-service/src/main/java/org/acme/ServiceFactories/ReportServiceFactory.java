package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.ReportService;

public class ReportServiceFactory {

    static ReportService service = null;
    public ReportService getReportService() {
        return (service != null) ? service : getNewReportService();
    }
    private ReportService getNewReportService() {
        var mq = new RabbitMqQueue("rabbitMq");
        return new ReportService(mq);
    }
}
