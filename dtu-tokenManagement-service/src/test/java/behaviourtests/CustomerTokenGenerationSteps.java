package behaviourtests;

import cucumber.api.PendingException;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import tokengeneration.service.Token;

public class CustomerTokenGenerationSteps {

    private Map<String,CompletableFuture<Event>> publishedEvents = new HashMap<>();

    private MessageQueue q = new MessageQueue() {

        @Override
        public void publish(Event event) {
            var token = event.getArgument(0, Token.class);
            publishedEvents.get(token.getName()).complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }

    };
    @Given("^that a customer is registered with DTUPay$")
    public void thatACustomerIsRegisteredWithDTUPay() {
    }

    @And("^the customer has one or less tokens$")
    public void theCustomerHasOneOrLessTokens() {
    }

    @When("^the customer requests a token$")
    public void theCustomerRequestsAToken() {
    }

    @Then("^\"([^\"]*)\" event is published$")
    public void eventIsPublished(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^the CustomerTokensAssigned event is received with customer token amount not exceeding (\\d+) tokens$")
    public void theCustomerTokensAssignedEventIsReceivedWithCustomerTokenAmountNotExceedingTokens(int arg0) {
    }

    @Then("^the customer tokens are registered$")
    public void theCustomerTokensAreRegistered() {
    }
}
