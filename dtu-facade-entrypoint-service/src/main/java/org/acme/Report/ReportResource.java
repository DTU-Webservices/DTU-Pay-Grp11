package org.acme.Report;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/reports")
public class ReportResource {

    private ReportService rs = new ReportServiceFactory().getReportService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Report createReport(Report report) {
        return rs.createReport(report);
    }
}
