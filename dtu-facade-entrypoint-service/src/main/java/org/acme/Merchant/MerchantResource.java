package org.acme.Merchant;

import org.acme.MoneyTransfer.Payment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/merchants")
public class MerchantResource {

    private MerchantService ms = new MerchantServiceFactory().getMerchantService();

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
        var result = ms.createPayment(payment);
        return Response.ok()
                .entity(result)
                .build();
    }

}
