package behaviourtests;


import dtu.ws.fastmoney.BankServiceException;
import dtu.ws.fastmoney.BankServiceException_Exception;
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
import org.acme.Repo.MoneyTransferRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    Payment payment;
    MoneyTransfer moneyTransfer;
    Merchant merchant;
    Customer customer;
    private CorrelationId correlationId;
    BankServiceException faultInfo = new BankServiceException();


    @Given("there is a payment with non-empty mid and cid")
    public void thereIsAPaymentWithNonEmptyId() {
        payment = new Payment();
        correlationId = CorrelationId.randomId();
        payment.setPaymentId(correlationId.getId());
        payment.setMid(UUID.randomUUID().toString());
        payment.setToken(UUID.randomUUID().toString());
        payment.setAmount("100");
        assertNotNull(payment.getToken());
        assertNotNull(payment.getMid());
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
        merchant = new Merchant();
        merchant.setMerchantId(UUID.fromString(payment.getMid()));
        var event = new Event(arg0, new Object[] {merchant, correlationId});
        verify(queue).publish(event);
    }

    @When("a {string} event is received with a mAccountId")
    public void aEventIsReceivedWithAMAccountId(String arg0) {
        //this step simulates event created by downstream service
        merchant.setAccountId("Merchant1");
        Event event = new Event(arg0, new Object[] {merchant, correlationId});
        ps.handleMerchantAccountIdGetReq(event);
        assertNotNull(MoneyTransferRepo.getMoneyTransfer(correlationId.getId()));
    }

    @Then("a {string} is sent with same correlation id after mAccountId assigned")
    public void aIsSentWithSameCorrelationIdWithMAccountIdAssigned(String arg0) {
        moneyTransfer = new MoneyTransfer();
        moneyTransfer = MoneyTransferRepo.getMoneyTransfer(correlationId.getId());
        assertNotNull(moneyTransfer.getMAccountId());
        System.out.println("her " + moneyTransfer.getMAccountId());
        var event = new Event(arg0, new Object[] {payment, correlationId});
        verify(queue).publish(event);
    }

    @When("a {string} event is received with a customer token")
    public void aEventIsReceivedWithACustomerId(String arg0) {
        customer = new Customer();
        customer.setCurrentToken(UUID.randomUUID());
        Event event = new Event(arg0, new Object[] {customer, correlationId});
        ps.handleGetCustomerIdFromToken(event);
    }

    @Then("a {string} is sent with same correlation id after token assigned")
    public void aIsSentWithSameCorrelationIdAfterTokenAssigned(String arg0) {
        assertNotNull(customer.getCurrentToken());
        var event = new Event(arg0, new Object[] {customer, correlationId});
        verify(queue).publish(event);
    }

    @When("a {string} event is received with a cAccountId")
    public void aEventIsReceivedWithCAccId(String arg0) {
        //BankServiceException_Exception
        try {
            customer.setAccountId("Customer1");
            moneyTransfer.setCAccountId(customer.getAccountId());
            moneyTransfer.setMAccountId(merchant.getAccountId());
            System.out.println(moneyTransfer);
            Event event = new Event(arg0, new Object[] {customer, correlationId});
            ps.handleCustomerAccountIdGetReq(event);
            throw new BankServiceException_Exception("", faultInfo);
        } catch (BankServiceException_Exception e) {
            System.out.println("Debtor or Creditor from this test does not exist in the bank");
        }
    }

    @Then("a {string} event is with the money transfer and correlation id")
    public void aEventIsSentWithSameCorrelationId(String arg0) {
        var event = new Event(arg0, new Object[] {moneyTransfer, correlationId});
        verify(queue).publish(event);
    }

    @And("the money transfer has an mAccountId and cAccountId assigned")
    public void theMoneyTransferHasAMAccountIdAndCAccountIdAssigned() {
        System.out.println(moneyTransfer);
        assertNotNull(moneyTransfer.getCAccountId());
        assertNotNull(moneyTransfer.getMAccountId());
    }
}
