package org.acme.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Path("/tokens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateTokens(Integer qty, String customerId) {
        cs.generateCustomerTokens(qty, customerId);
        return Response.ok()
                .entity(qty + " tokens generated for customer " + customerId)
                .build();
    }

    @GET
    @Path("/tokens/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@PathParam("customerId") String customerId) {
        cs.getCustomerToken(customerId);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer registerCustomer(Customer customer) {
        return cs.registerCustomer(customer);
    }


}
