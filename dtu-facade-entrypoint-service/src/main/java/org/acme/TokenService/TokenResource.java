package org.acme.TokenService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tokens")
public class TokenResource {

    private TokenService ts = new TokenServiceFactory().getTokenService();

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Token getToken(@PathParam("accountId") String accountId) {
        return ts.getToken(accountId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Token generateToken(Token token) {
        return ts.generateToken(token);
    }
}
