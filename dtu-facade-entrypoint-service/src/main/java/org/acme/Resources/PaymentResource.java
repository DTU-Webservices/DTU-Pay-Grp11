package org.acme.Resources;

import org.acme.Entities.MoneyTransfer;
import org.acme.Entities.Payment;
import org.acme.ServiceFactories.PaymentServiceFactory;
import org.acme.Services.PaymentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @Author Kristoffer T. Pedersen, Lauritz Pepke
 */
@Path("/payments")
public class PaymentResource {

    private final PaymentService ps = new PaymentServiceFactory().getPaymentService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoneyTransfer createPayment(Payment payment) {
        return ps.createPayment(payment);
    }
}
