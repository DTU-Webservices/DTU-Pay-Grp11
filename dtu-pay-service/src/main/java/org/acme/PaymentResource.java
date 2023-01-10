package org.acme;

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Objects;


@Path("/payment")
public class PaymentResource {



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPaymentJson() {
        return "hello";
    }

}

