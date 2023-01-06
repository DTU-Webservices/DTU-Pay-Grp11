package org.acme;

import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;


@Path("/payment")
public class PaymentResource {

    private final PaymentService service = new PaymentService();

    @POST
    @Path("/createAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(User user) {
    	try {
    		String acc = service.createBankAccount(user, BigDecimal.valueOf(1000));
    		return Response.ok()
                    .entity(acc)
                    .build();
    	} catch (BankServiceException_Exception e) {
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    }

    @POST
    @Path("/retireAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response retireAccount(String accountId) {
    	try {
    		service.retireAccount(accountId);
    		return Response.ok()
                    .build();
    	} catch (BankServiceException_Exception e) {
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
    	}
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Payment getPaymentJson() {
        return service.getPayment();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Payment getPaymentXml() {
        return service.getPayment();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsJson() {
        return Response.ok(service.getPayments()).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pay(Payment p) {
        String cid = service.getCid(p);
        String mid = service.getMid(p);
        String messageCid = "customer with id " + cid + " is unknown";
        String messageMid = "merchant with id " + mid + " is unknown";
        if (Objects.equals(mid, "mid1") && !Objects.equals(cid, "cid1")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messageCid)
                    .build();
        } else if (Objects.equals(cid, "cid1") && !Objects.equals(mid, "mid1")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messageMid)
                    .build();
        } else {
            service.pay(p);
            return Response.ok()
                    .entity("payment successful")
                    .build();
        }
    }
}

/*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response payJson(Payment p) throws URISyntaxException {
        if (service.validatePayment(p)) {
            service.pay(p);
            System.out.println("Payment successful");
            return Response.created(new URI("/payment")).build();
        } else {
            System.out.println("Payment failed");
            return Response.status(Response.Status.BAD_REQUEST).build();

        }
    }

 */
