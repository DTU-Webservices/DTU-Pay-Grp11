package tokengeneration.service.adapter.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import tokengeneration.service.Token;
import tokengeneration.service.TokenGenerationService;

@Path("/tokens")
public class TokenResource {

    private TokenGenerationService service = new TokenGenerationFactory().getService();

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Token generateToken(Token token) {
        return service.generateToken(token);
    }
}
