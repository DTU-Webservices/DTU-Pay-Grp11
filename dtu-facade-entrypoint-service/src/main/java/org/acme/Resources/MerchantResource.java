package org.acme.Resources;

import org.acme.Entities.Merchant;
import org.acme.Entities.Report;
import org.acme.ServiceFactories.MerchantServiceFactory;
import org.acme.Entities.Payment;
import org.acme.ServiceFactories.ReportServiceFactory;
import org.acme.Services.MerchantService;
import org.acme.Services.PaymentService;
import org.acme.ServiceFactories.PaymentServiceFactory;
import org.acme.Services.ReportService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Oliver Brink Klenum s193625
 *
 */
@Path("/merchants")
public class MerchantResource {

    private final MerchantService ms = new MerchantServiceFactory().getMerchantService();
    private final PaymentService ps = new PaymentServiceFactory().getPaymentService();

    private ReportService rs = new ReportServiceFactory().getReportService();

    @GET
    @Path("/{merchantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Merchant getMerchant(@PathParam("merchantId") String merchantId) {
        return ms.getMerchant(merchantId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Merchant registerMerchant(Merchant merchant) {
        return ms.registerMerchant(merchant);
    }

    @DELETE
    @Path("/{merchantId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteMerchant(@PathParam("merchantId") String merchantId) {
        if (ms.deleteMerchant(merchantId)) {
            return Response.ok("Merchant deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPayment(Payment payment) {
        var result = ps.createPayment(payment);
        return Response.ok()
                .entity(result)
                .build();
    }

    @GET
    @Path("/reports/{merchantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Report createReportWithMerchantPayments(@PathParam("merchantId") String merchantId) {
        return rs.getAllPaymentsMadeByMerchant(merchantId);
    }
}
