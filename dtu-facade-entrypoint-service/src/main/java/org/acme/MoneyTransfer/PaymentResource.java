package org.acme.MoneyTransfer;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/payments")
public class PaymentResource {

    private PaymentService ps = new PaymentServiceFactory().getPaymentService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoneyTransfer createPayment(Payment payment) {
        return ps.createPayment(payment);
    }
}
