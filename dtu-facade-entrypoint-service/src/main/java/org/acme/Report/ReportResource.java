package org.acme.Report;

import org.acme.MoneyTransfer.MoneyTransfer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/reports")
public class ReportResource {

    private final ReportService rs = new ReportServiceFactory().getReportService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Report createReportWithAllPayments() {
        return rs.getAllPayments();
    }

    @Path("/{CAccountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Report createReportWithCustomerPayments() { return rs.getAllPaymentsMadeByCustomer();}
}
