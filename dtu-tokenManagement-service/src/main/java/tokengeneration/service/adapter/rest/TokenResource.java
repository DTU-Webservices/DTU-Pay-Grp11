package tokengeneration.service.adapter.rest;

import javax.ws.rs.*;

import tokengeneration.service.Token;
import tokengeneration.service.TokenGenerationService;

@Path("/tokens")
public class TokenResource {

    private TokenGenerationService service = new TokenGenerationFactory().getService();

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Token generateToken(Token token) {
        return service.generate(token);
    }

}
