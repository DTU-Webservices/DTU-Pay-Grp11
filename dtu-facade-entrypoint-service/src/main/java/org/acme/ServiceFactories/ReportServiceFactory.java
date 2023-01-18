package org.acme.ServiceFactories;

import messaging.implementations.RabbitMqQueue;
import org.acme.Services.ReportService;

/**
 * Inspiration drawn from Hubert Baumeiser 02267 Correlation Code Example
 * @author Oliver Brink Klenum s193625.
 * @author Tobias Stærmose xxxxxxx.
 *
 */
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
