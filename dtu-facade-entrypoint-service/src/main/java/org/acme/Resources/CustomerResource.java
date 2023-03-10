package org.acme.Resources;

import org.acme.Entities.Customer;
import org.acme.Entities.Report;
import org.acme.ServiceFactories.CustomerServiceFactory;
import org.acme.ServiceFactories.ReportServiceFactory;
import org.acme.ServiceFactories.TokenServiceFactory;
import org.acme.Services.CustomerService;
import org.acme.Entities.Token;
import org.acme.Services.ReportService;
import org.acme.Services.TokenService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke s191179.
 * @author Oliver Brink Klenum s193625
 * @author Tobias Stærmose s205356
 *
 */

@Path("/customers")
public class CustomerResource {

    private CustomerService cs = new CustomerServiceFactory().getCustomerService();
    private ReportService rs = new ReportServiceFactory().getReportService();
    private TokenService ts = new TokenServiceFactory().getTokenService();

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomer(@PathParam("customerId") String customerId) {
        return cs.getCustomer(customerId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer registerCustomer(Customer customer) {
        return cs.registerCustomer(customer);
    }

    @POST
    @Path("/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateTokens(Token token) {
        var tokenRes = ts.generateTokens(token);
        System.out.println(tokenRes);
        return Response.ok()
                .entity(tokenRes.getQty() +" tokens generated to " + tokenRes.getCustomerId())
                .build();
    }

    @DELETE
    @Path("/{customerId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCustomer(@PathParam("customerId") String customerId) {
        if (cs.deleteCustomer(customerId)) {
            return Response.ok("Customer deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/tokens/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokenForPayment(@PathParam("customerId") String customerId) {
        var token = ts.getTokenForPayment(UUID.fromString(customerId));
        var tokensAmount = ts.getTokensAmount(UUID.fromString(customerId));
        if (tokensAmount == null || customerId.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok()
                    .entity(token.getCurrentToken())
                    .build();
        }
    }

    @GET
    @Path("/reports/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Report createReportWithCustomerPayments(@PathParam("customerId") String customerId) {
        return rs.getAllPaymentsMadeByCustomer(customerId);
    }
}
