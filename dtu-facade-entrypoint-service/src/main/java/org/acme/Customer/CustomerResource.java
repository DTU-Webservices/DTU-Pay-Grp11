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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer registerCustomer(Customer customer) {
        return cs.registerCustomer(customer);
    }


}
