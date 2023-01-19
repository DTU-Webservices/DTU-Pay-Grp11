package e2eTest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


public class WebTargetFactory {
    static Client client;

    public static WebTarget getWebTarget() {

        client = ClientBuilder.newClient();
        return client.target("http://localhost:8080/");
    }

    public static void close() {
        client.close();
    }
}
