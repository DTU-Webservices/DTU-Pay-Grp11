package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.Payment;
import org.acme.PaymentService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * SOURCE: HUBERT BAUMEISTER: STUDENT_REGISTRATION_CORRELATION PROJECT
 */

public class MoneyTransferSteps {

    private Map<String, CompletableFuture<Event>> publishedEvents = new HashMap<>();

    private MessageQueue q = new MessageQueue() {
        @Override
        public void publish(Event event) {
            var payment = event.getArgument(0, Payment.class);
            publishedEvents.get(payment.getAmount()).complete(event);

        }

        @Override
        public void addHandler(String eventType, Consumer<Event> handler) {

        }
    };
    Payment payment;
    Payment expected;
    private PaymentService paymentService = new PaymentService(q);
    private CompletableFuture<Payment> registeredPayment = new CompletableFuture<>();
    private Map<Payment, CorrelationId> correlationIds = new HashMap<>();



    @Given("there is a payment with empty amount, token and mid")
    public void thereIsAPaymentWithEmptyAmountTokenAndMid() {
        payment = new Payment();
        payment.setAmount("10");
        publishedEvents.put(payment.getAmount(), new CompletableFuture<Event>());
        System.out.println("step 1: " + publishedEvents);
        assertNull(payment.getCid());
        assertNull(payment.getMid());
    }

    @When("the payment is being initiated")
    public void thePaymentIsBeingInitiated() {
        new Thread(() -> {
            //var result = paymentService.assignAmount(payment);
            //registeredPayment.complete(result);
        }).start();
    }

    //testen passer kun på amount, da de andre events ikke er implementerede endnu
    @Then("a {string} event is sent")
    public void aEventIsSent(String arg0) {
        //skal rettes
        Event event = publishedEvents.get(payment.getAmount()).join();
        assertEquals(arg0, event.getType());
        var st = event.getArgument(0, Payment.class);
        var correlationId = event.getArgument(1, CorrelationId.class);
        correlationIds.put(st, correlationId);
        System.out.print("step 3: " + correlationIds);
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

    @Then("the amount is deducted from the Customer bank account")
    public void theAmountIsDeductedFromTheCustomerBankAccount() {
    }

    @And("the amount is added to the Merchant bank account")
    public void theAmountIsAddedToTheMerchantBankAccount() {
    }
}
