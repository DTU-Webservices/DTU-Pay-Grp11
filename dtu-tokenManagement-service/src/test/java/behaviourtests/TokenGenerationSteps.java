package behaviourtests;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import tokengeneration.service.CorrelationId;
import tokengeneration.service.Token;
import tokengeneration.service.TokenGenerationService;

public class TokenGenerationSteps {

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
    private CompletableFuture<Token> generatedToken2 = new CompletableFuture<>();
    private Token token;
    private Token token2;
    private Map<Token,CorrelationId> correlationIds = new HashMap<>();

    public TokenGenerationSteps() {
    }

    @Given("there is a token with empty id")
    public void thereIsATokenWithEmptyId() {
        token = new Token();
        token.setCid("123456789");
        publishedEvents.put(token.getCid(), new CompletableFuture<Event>());
        assertNull(token.getId());
    }

    @When("the token is being registered")
    public void theTokenIsBeingRegistered() {
        // We have to run the registration in a thread, because
        // the register method will only finish after the next @When
        // step is executed.
        new Thread(() -> {
            var result = service.generate(token);
            generatedToken.complete(result);
        }).start();
    }

    @Then("the {string} event is published")
    public void theEventIsPublished(String string) {
        Event event = publishedEvents.get(token.getCid()).join();
        assertEquals(string, event.getType());
        var token = event.getArgument(0, Token.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        correlationIds.put(token, correlationId);
    }

    @When("the TokenIdAssigned event is received with non-empty id")
    public void theTokenIdAssignedEventIsReceivedWithNonEmptyId() {

    }

    @Then("the token is registered and the id is set")
    public void theTokenIsRegisteredAndTheIdIsSet() {
        var result = generatedToken.join();
        assertNotNull(result.getId());
    }
}
