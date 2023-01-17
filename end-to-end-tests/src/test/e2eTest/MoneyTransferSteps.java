package e2eTest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNull;

public class MoneyTransferSteps {

    private CompletableFuture<Event> publishedEvent = new CompletableFuture<>();


    private MessageQueue q = new MessageQueue() {

        @Override
        public void publish(Event event) {
            publishedEvent.complete(event);
        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {
        }

    };

    private Payment payment;

    @Given("there is a payment with empty amount, token and mid")
    public void thereIsAPaymentWithEmptyAmountTokenAndMid() {
        payment = new Payment();
        payment.setAmount("125");
        assertNull(payment.getPaymentId());
    }

    @When("the payment is being initiated")
    public void thePaymentIsBeingInitiated() {
    }

    @Then("a {string} event is sent")
    public void aEventIsSent(String arg0) {
    }

    @When("a {string} event for the payment is sent with non-empty amount")
    public void aEventForThePaymentIsSentWithNonEmptyAmount(String arg0) {
    }

    @And("a {string} event for the payment is sent with non-empty mid")
    public void aEventForThePaymentIsSentWithNonEmptyMid(String arg0) {
    }

    @And("a {string} event for the payment is sent with non-empty token")
    public void aEventForThePaymentIsSentWithNonEmptyToken(String arg0) {
    }

    @Then("the payment is set with an amount, a token and a mid")
    public void thePaymentIsSetWithAnAmountATokenAndAMid() {
    }

    @When("the {string} event for a payment is sent")
    public void theEventForAPaymentIsSent(String arg0) {
    }

    @Then("the transfer has been made")
    public void theTransferHasBeenMade() {
    }
}
