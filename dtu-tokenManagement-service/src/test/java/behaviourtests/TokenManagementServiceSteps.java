package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.Customer;
import org.acme.Entity.Payment;
import org.acme.Entity.Token;
import org.acme.Repo.TokenRepo;
import org.acme.TokenService;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Oliver Brink Klenum s193625
 */
public class TokenManagementServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);

    TokenService ts = new TokenService(queue);
    Token token1;
    Token token2;
    Token token3;
    Token token4;
    Payment payment1;
    Customer customer1;
    Token expected1;
    Token expected2;
    Token expected3;
    Token expected4;
    Payment expectedPayment1;
    Customer expectedCustomer1;
    private CorrelationId correlationId1;
    private CorrelationId correlationId2;
    private CorrelationId correlationId3;
    private CorrelationId correlationId4;


    @When("a {string} event for a token is received")
    public void aEventIsReceived(String string) {
        token1 = new Token();
        token1.setCustomerId(UUID.fromString("d541aa85-6fa8-4b4a-9719-c67929d8acdf"));
        token1.setQty("1");
        token1.setTokenId(null);
        assertNull(token1.getTokenId());
        correlationId1 = CorrelationId.randomId();
        ts.handleTokensGenerate(new Event(string, new Object[] {token1, correlationId1}));
    }

    @Then("a {string} event is sent with the same correlation id")
    public void aEventIsSent(String string) {
        expected1 = new Token();
        expected1.setCustomerId(UUID.fromString("d541aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected1.setTokenId(correlationId1.getId());
        expected1.setQty("1");
        expected1.setTokens(TokenRepo.getToken(UUID.fromString("d541aa85-6fa8-4b4a-9719-c67929d8acdf")).getTokens());
        var event = new Event(string, new Object[] {expected1, correlationId1});
        verify(queue).publish(event);
    }

    @And("the token is generated")
    public void theTokenIsAssignedToTheCustomer() {
        System.out.println("Token: " + expected1.getTokens().get(0) + " for CustomerId: " + expected1.getCustomerId());
        assertNotNull(expected1.getCustomerId());
        assertNotNull(expected1.getTokens().get(0));
    }

    @Given("there is a customer registered with available tokens")
    public void thereIsACustomerRegisteredWithAvailableTokens() {
        token2 = new Token();
        token2.setCustomerId(UUID.fromString("d007aa85-6fa8-4b4a-9719-c67929d8acdf"));
        correlationId2 = CorrelationId.randomId();
        token2.setTokenId(correlationId2.getId());
        token2.addToken(UUID.fromString("d843aa85-6fa8-4b4a-9719-c67929d8acdf"));
        token2.setQty("1");
        token2.setCurrentToken(String.valueOf(token2.getTokens().get(0)));
        TokenRepo.addToken(token2);
        assertNotNull(TokenRepo.getToken(token2.getCustomerId()));
    }

    @When("a {string} event for a customer id is received")
    public void aEventForACustomerIdIsReceived(String string) {
        Event event = new Event(string, new Object[] {token2, correlationId2});
        ts.handleTokensGetToken(event);
    }

    @Then("a {string} event is sent with the same correlation id and the token")
    public void aEventIsSentWithTheSameCorrelationId(String string) {
        expected2 = new Token();
        expected2.setCustomerId(UUID.fromString("d007aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected2.setTokenId(correlationId2.getId());
        expected2.addToken(UUID.fromString("d843aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected2.setQty("1");
        expected2.setCurrentToken(String.valueOf(expected2.getTokens().get(0)));
        var event = new Event(string, new Object[] {expected2, correlationId2});
        verify(queue).publish(event);
    }

    @And("the token is returned")
    public void theTokenIsReturned() {
        System.out.println("Token Available: " + expected2.getTokens().get(0));
        assertNotNull(expected2.getTokens().get(0));
    }


    @Given("there is a customer registered with tokens")
    public void thereIsACustomerRegisteredWithTokens() {
        token3 = new Token();
        token3.setCustomerId(UUID.fromString("d111aa85-6fa8-4b4a-9719-c67929d8acdf"));
        correlationId3 = CorrelationId.randomId();
        token3.setTokenId(correlationId3.getId());
        token3.addToken(UUID.fromString("d444aa85-6fa8-4b4a-9719-c67929d8acdf"));
        token3.addToken(UUID.fromString("d555aa85-6fa8-4b4a-9719-c67929d8acdf"));
        token3.addToken(UUID.fromString("d888aa85-6fa8-4b4a-9719-c67929d8acdf"));
        TokenRepo.addToken(token3);
        assertNotNull(TokenRepo.getToken(token3.getCustomerId()));
    }

    @When("a {string} event for a customer is received")
    public void aEventForACustomerIsReceived(String string) {
        Event event = new Event(string, new Object[] {token3, correlationId3});
        ts.handleTokensAmountGet(event);
    }

    @Then("a {string} event is sent with the same correlation id and the amount of tokens")
    public void aEventIsSentWithTheSameCorrelationIdAndTheAmountOfTokens(String string) {
        expected3 = new Token();
        expected3.setCustomerId(UUID.fromString("d111aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected3.setTokenId(correlationId3.getId());
        expected3.addToken(UUID.fromString("d444aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected3.addToken(UUID.fromString("d555aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected3.addToken(UUID.fromString("d888aa85-6fa8-4b4a-9719-c67929d8acdf"));
        var event = new Event(string, new Object[] {expected3, correlationId3});
        verify(queue).publish(event);
    }

    @And("the amount of tokens is returned")
    public void theAmountOfTokensIsReturned() {
        System.out.println("Amount of tokens available: " + expected3.getTokens().size());
        assertEquals(3, expected3.getTokens().size());
    }

    @Given("there is a customer registered with a token")
    public void thereIsACustomerRegisteredWithAToken() {
        token4 = new Token();
        token4.setCustomerId(UUID.fromString("d999aa85-6fa8-4b4a-9719-c67929d8acdf"));
        correlationId4 = CorrelationId.randomId();
        token4.setTokenId(correlationId4.getId());
        token4.addToken(UUID.fromString("d254aa85-6fa8-4b4a-9719-c67929d8acdf"));
        TokenRepo.addToken(token4);
        assertNotNull(TokenRepo.getToken(token4.getCustomerId()));
    }

    @Given("there is a payment registered with a token")
    public void thereIsAPaymentRegisteredWithAToken() {
        token4 = new Token();
        token4.setCustomerId(UUID.fromString("c999aa85-6fa8-4b4a-9719-c67929d8acdf"));
        correlationId4 = CorrelationId.randomId();
        token4.setTokenId(correlationId4.getId());
        token4.addToken(UUID.fromString("c254aa85-6fa8-4b4a-9719-c67929d8acdf"));
        TokenRepo.addToken(token4);
        payment1 = new Payment();
        payment1.setPaymentId(UUID.fromString("c123aa85-6fa8-4b4a-9719-c67929d8acdf"));
        payment1.setToken("c254aa85-6fa8-4b4a-9719-c67929d8acdf");
        payment1.setAmount("100");
        payment1.setMid("123456789");
        customer1 = new Customer();
        UUID customerId = TokenRepo.getCidToken(UUID.fromString(payment1.getToken()));
        customer1.setCustomerId(customerId);
        customer1.setCurrentToken(UUID.fromString(payment1.getToken()));
        assertNotNull(TokenRepo.getToken(token4.getCustomerId()));
    }

    @When("a {string} event for a customer token is received")
    public void aEventForACustomerTokenIsReceived(String string) {
        Event event = new Event(string, new Object[] {payment1, correlationId4});
        ts.handleGetCustomerIdFromToken(event);
    }

    @Then("a {string} event is sent with the same correlation id and the customer id")
    public void aEventIsSentWithTheSameCorrelationIdAndTheCustomerId(String string) {
        expected4 = new Token();
        expected4.setCustomerId(UUID.fromString("c999aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expected4.setTokenId(correlationId4.getId());
        expected4.addToken(UUID.fromString("c254aa85-6fa8-4b4a-9719-c67929d8acdf"));
        expectedPayment1 = new Payment();
        expectedPayment1.setToken("c254aa85-6fa8-4b4a-9719-c67929d8acdf");
        expectedCustomer1 = new Customer();
        expectedCustomer1.setCustomerId(expected4.getCustomerId());
        expectedCustomer1.setCurrentToken(UUID.fromString(expectedPayment1.getToken()));
        //TokenRepo.getToken(customerId).removeToken(UUID.fromString(expectedPayment1.getToken()));
        var event = new Event(string, new Object[] {expectedCustomer1, correlationId4});
        verify(queue).publish(event);
    }

    @And("the customer id is returned")
    public void theCustomerIdIsReturned() {
        assertNotNull(expected4.getCustomerId());
        System.out.println("CustomerId Returned: " + expected4.getCustomerId());
    }
}
