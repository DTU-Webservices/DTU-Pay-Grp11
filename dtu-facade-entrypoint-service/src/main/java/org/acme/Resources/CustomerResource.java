package org.acme.Resources;

import org.acme.Entities.Customer;
import org.acme.ServiceFactories.CustomerServiceFactory;
import org.acme.Services.CustomerService;
import org.acme.Entities.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke s191179.
 * @author Oliver Brink Klenum s193625
 */

@Path("/customers")
public class CustomerResource {

    private CustomerService cs = new CustomerServiceFactory().getCustomerService();

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
        cs.generateCustomerTokens(token);
        return Response.ok()
                .entity(token.getQty() +" tokens generated to " + token.getCustomerId())
                .build();
    }

    @GET
    @Path("/tokens/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokensAmount(@PathParam("customerId") String customerId) {
        //var token = cs.getCustomerTokensAmount(customerId).getTokens().size();
        var token = cs.getCustomerTokensAmount(UUID.fromString(customerId));
        if (token == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok()
                    .entity(token.getTokens().size() + " tokens available for " + token.getCustomerId())
                    .build();
        }
    }
}
