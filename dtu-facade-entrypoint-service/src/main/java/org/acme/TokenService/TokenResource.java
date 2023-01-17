package org.acme.TokenService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 *
 * @author Oliver Brink Klenum s193625
 *
 */

@Path("/tokens")
public class TokenResource {

    private TokenService ts = new TokenServiceFactory().getTokenService();

    // Gets all tokens for a given customer
    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Token getToken(@PathParam("customerId") String customerId) {
        return ts.getTokenByCustomerId(UUID.fromString(customerId));
    }


    @GET
    @Path("/payment/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokenForPayment(@PathParam("customerId") String customerId) {
        var token = ts.getTokenForPayment(UUID.fromString(customerId));
        if (token.getTokenId() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(token.getTokens().get(0)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Token generateTokens(Token token) {
        return ts.generateTokens(token);
    }

}
