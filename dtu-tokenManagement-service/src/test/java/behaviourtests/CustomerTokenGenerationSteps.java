package behaviourtests;


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
import tokengeneration.service.CorrelationId;
import tokengeneration.service.Token;
import tokengeneration.service.TokenGenerationService;

public class CustomerTokenGenerationSteps {

    private Map<String,CompletableFuture<Event>> publishedEvents = new HashMap<>();

    private MessageQueue q = new MessageQueue() {

        @Override
        public void publish(Event event) {
            var token = event.getArgument(0, Token.class);
            publishedEvents.get(token.getCid()).complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }

    };

    private TokenGenerationService service = new TokenGenerationService(q);
    private CompletableFuture<Token> generatedToken = new CompletableFuture<>();
    private Token token;
    private Map<Token, CorrelationId> correlationIds = new HashMap<>();

    public CustomerTokenGenerationSteps() {
    }


    @Given("^that a customer is registered with DTUPay$")
    public void thatACustomerIsRegisteredWithDTUPay() {
        token = new Token();
        token.setCid("123456789");
        publishedEvents.put(token.getCid(), new CompletableFuture<Event>());
        assertNull(token.getToken());
    }

    @And("^the customer has one or less tokens$")
    public void theCustomerHasOneOrLessTokens() {

    }

    @When("^the customer requests a token$")
    public void theCustomerRequestsAToken() {
        // We have to run the registration in a thread, because
        // the register method will only finish after the next @When
        // step is executed.
        new Thread(() -> {
            CorrelationId cid = new CorrelationId(UUID.randomUUID());
            correlationIds.put(token, cid);
            service.generateToken(token);
        }).start();
    }

    @Then("^\"([^\"]*)\" event is published$")
    public void eventIsPublished(String string) {
        // Write code here that turns the phrase above into concrete actions
        Event event = publishedEvents.get(token.getCid()).join();
        assertEquals(string, event.getType());
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        correlationIds.put(token, correlationId);

    }

    @When("the CustomerTokenAssigned event is received with customer token amount not exceeding {int} tokens")
    public void the_customer_token_assigned_event_is_received_with_customer_token_amount_not_exceeding_tokens(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        var t = new Token();
        t.setCid("123456789");
        t.setToken("2345");
        t.setId(1);
        service.handleCustomerTokenAssigned(new Event(TokenGenerationService.CUSTOMER_TOKEN_ASSIGNED, new Object[] { t, correlationIds.get(token) }));
        throw new io.cucumber.java.PendingException();
    }


    @Then("^the customer tokens are registered$")
    public void theCustomerTokensAreRegistered() {
        assertNotNull(generatedToken.join().getToken());
    }
}
