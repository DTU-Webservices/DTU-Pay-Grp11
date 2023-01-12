package org.acme.Demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/demo")
public class DemoResource {

    DemoService ds = new DemoServiceFactory().getDemoService();
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return ds.getDemoMessage().getDemoMessage();
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Hello this is a testAAAA";
    }
}