package org.acme.Resources;

import org.acme.Entities.Report;
import org.acme.ServiceFactories.ReportServiceFactory;
import org.acme.Services.ReportService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/reports")
public class ReportResource {

    private final ReportService rs = new ReportServiceFactory().getReportService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Report createReportWithAllPayments() {
        return rs.getAllPayments();
    }
}
