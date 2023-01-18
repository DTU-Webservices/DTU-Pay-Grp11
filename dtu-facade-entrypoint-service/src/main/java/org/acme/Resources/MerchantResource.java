package org.acme.Resources;

import org.acme.Entities.Merchant;
import org.acme.ServiceFactories.MerchantServiceFactory;
import org.acme.Entities.Payment;
import org.acme.Services.MerchantService;
import org.acme.Services.PaymentService;
import org.acme.ServiceFactories.PaymentServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchants")
public class MerchantResource {

    private MerchantService ms = new MerchantServiceFactory().getMerchantService();
    private final PaymentService ps = new PaymentServiceFactory().getPaymentService();

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
}
