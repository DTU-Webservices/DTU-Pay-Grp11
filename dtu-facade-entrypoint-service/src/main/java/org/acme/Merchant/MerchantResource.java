package org.acme.Merchant;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
}
