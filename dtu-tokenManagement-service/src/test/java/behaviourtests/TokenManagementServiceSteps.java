package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.Token;
import org.acme.Repo.TokenRepo;
import org.acme.TokenService;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
public class TokenManagementServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);

    TokenService ts = new TokenService(queue);
    Token token1;
    Token expected1;
    Token token2;
    Token expected2;
    private CorrelationId correlationId1;
    private CorrelationId correlationId2;


    @When("a {string} event for a token is received")
    public void aEventIsReceived(String string) {
        token1 = new Token();
        token1.setCustomerId(UUID.fromString("123e2167-e89b-12t3-a456-42665544"));
        token1.setQty("4");
        assertNull(token1.getTokenId());
        correlationId1 = CorrelationId.randomId();
        ts.handleTokensGenerate(new Event(string, new Object[] {token1, correlationId1}));
    }

    @Then("a {string} event is sent with the same correlation id")
    public void aEventIsSent(String string) {
        expected1 = new Token();
        expected1.setCustomerId(UUID.fromString("123e2167-e89b-12t3-a456-42665544"));
        expected1.setTokenId(correlationId1.getId());
        System.out.println(expected1);
        var event = new Event(string, new Object[] {expected1, correlationId1});
        verify(queue).publish(event);
    }

    @And("the token is generated")
    public void theTokenIsAssignedToTheCustomer() {
        System.out.println(expected1.getTokenId());
        assertNotNull(expected1.getTokenId());
    }
}
