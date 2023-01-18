package behaviourtests;

import io.cucumber.java.an.E;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.*;
import org.acme.Entity.Customer;
import org.acme.Entity.Merchant;
import org.acme.Entity.MoneyTransfer;
import org.acme.Entity.Payment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * SOURCE: HUBERT BAUMEISTER: STUDENT_REGISTRATION_CORRELATION PROJECT
 */

public class MoneyTransferSteps {


    private Map<String, CompletableFuture<Event>> publishedEvents = new HashMap<>();
    MessageQueue queue = mock(MessageQueue.class);

    PaymentService ps = new PaymentService(queue);

    private CompletableFuture<Payment> registeredPayment = new CompletableFuture<>();
    private Map<Payment, CorrelationId> correlationIds = new HashMap<>();
    Payment payment;
    Payment expectedPayment;
    MoneyTransfer moneyTransfer;
    MoneyTransfer expectedMoneyTransfer;
    Merchant merchant;
    Merchant expectedMerchant;
    Customer customer;
    Customer expectedCustomer;
    private CorrelationId correlationId;


    @Given("there is a payment with non-empty mid and cid")
    public void thereIsAPaymentWithNonEmptyId() {
       /* payment = new Payment();
        correlationId = CorrelationId.randomId();
        payment.setPaymentId(correlationId.getId());
        payment.setMid(UUID.randomUUID().toString());
        payment.setCid(UUID.randomUUID().toString());
        payment.setAmount("100");
        assertNotNull(payment.getCid());
        assertNotNull(payment.getMid());*/
    }

    @And("there is a money transfer with empty mAccountId and cAccountId")
    public void thereIsAMoneyTransferWithEmptyId() {
        moneyTransfer = new MoneyTransfer();
        moneyTransfer.setMtId(UUID.randomUUID());
        moneyTransfer.setAmount(payment.getAmount());
        assertNull(moneyTransfer.getCAccountId());
        assertNull(moneyTransfer.getMAccountId());
    }

    @When("a {string} event for a payment is received")
    public void aEventIsReceived(String arg0) {
        Event event = new Event(arg0, new Object[] {payment, correlationId});
        ps.handlePaymentRequested(event);
    }

    @Then("a {string} is sent with same correlation id")
    public void aIsSentWithSameCorrelationId(String arg0) {
        /*expectedMerchant = new Merchant();
        expectedMerchant.setMerchantId(UUID.fromString(payment.getMid()));
        expectedMerchant.setAccountId("1");
        var event = new Event(arg0, new Object[] {expectedMerchant, correlationId});
        verify(queue).publish(event);*/
    }

    @When("a {string} event is received with a mAccountId")
    public void aEventIsReceivedWithAMAccountId(String arg0) {
    }

    @When("a {string} event is received with a cAccountId")
    public void aEventIsReceivedWithCAccId(String arg0) {
    }

    @Then("a {string} event is sent")
    public void aEventIsSent(String arg0) {
    }
}
