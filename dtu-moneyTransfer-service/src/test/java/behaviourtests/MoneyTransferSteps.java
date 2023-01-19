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
import org.acme.Entity.*;
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
    MoneyTransfer moneyTransfer1, moneyTransfer2;
    Merchant merchant;
    Customer customer;
    Report report;
    Report expectedReport;
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
        moneyTransfer1 = new MoneyTransfer();
        moneyTransfer1.setMtId(UUID.randomUUID());
        moneyTransfer1.setAmount(payment.getAmount());
        assertNull(moneyTransfer1.getCAccountId());
        assertNull(moneyTransfer1.getMAccountId());
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
        moneyTransfer1 = new MoneyTransfer();
        moneyTransfer1 = MoneyTransferRepo.getMoneyTransfer(correlationId.getId());
        assertNotNull(moneyTransfer1.getMAccountId());
        System.out.println("her " + moneyTransfer1.getMAccountId());
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
            moneyTransfer1.setCAccountId(customer.getAccountId());
            moneyTransfer1.setMAccountId(merchant.getAccountId());
            System.out.println(moneyTransfer1);
            Event event = new Event(arg0, new Object[] {customer, correlationId});
            ps.handleCustomerAccountIdGetReq(event);
            throw new BankServiceException_Exception("", faultInfo);
        } catch (BankServiceException_Exception e) {
            System.out.println("Debtor or Creditor from this test does not exist in the bank");
        }
    }

    @Then("a {string} event is with the money transfer and correlation id")
    public void aEventIsSentWithSameCorrelationId(String arg0) {
        var event = new Event(arg0, new Object[] {moneyTransfer1, correlationId});
        verify(queue).publish(event);
    }

    @And("the money transfer has an mAccountId and cAccountId assigned")
    public void theMoneyTransferHasAMAccountIdAndCAccountIdAssigned() {
        System.out.println(moneyTransfer1);
        assertNotNull(moneyTransfer1.getCAccountId());
        assertNotNull(moneyTransfer1.getMAccountId());
    }

    @Given("there is a money transfer with non-empty mAccountId, cAccountId and amount")
    public void thereIsAMoneyTransferWithNonEmptyMAccountIdCAccountIdAndAmount() {
        moneyTransfer2 = new MoneyTransfer();
        correlationId = CorrelationId.randomId();
        //simulate money transfer values assignment
        moneyTransfer2.setAmount("100");
        moneyTransfer2.setMAccountId(UUID.randomUUID().toString());
        moneyTransfer2.setCAccountId(UUID.randomUUID().toString());
        MoneyTransferRepo.addMoneyTransfer(moneyTransfer2);
        assertNotNull(moneyTransfer2.getMAccountId());
        assertNotNull(moneyTransfer2.getCAccountId());
        assertNotNull(moneyTransfer2.getAmount());
    }

    @When("a {string} event is received for a report with correlation id")
    public void aEventIsReceivedForAReportWithCorrelationId(String arg0) {
        System.out.println(correlationId);
        Event event = new Event(arg0, new Object[] {correlationId});
        ps.handleAllPaymentsReportRequest(event);
    }

    @Then("a {string} event is sent with same correlation id")
    public void aReportEventIsSentWithSameCorrelationId(String arg0) {
        expectedReport = new Report();
        expectedReport.setMoneyTransfers(MoneyTransferRepo.getAllPayments());
        expectedReport.setTotalAmount(MoneyTransferRepo.getTotalAmount());
        expectedReport.setReportId(correlationId.getId());
        var event = new Event(arg0, new Object[] {expectedReport, correlationId});
        verify(queue).publish(event);
    }

    @And("a report with all payments is generated")
    public void aReportWithAllPaymentsIsGenerated() {
        assertNotNull(expectedReport.getReportId());
    }

    @When("a {string} event is received for a customer")
    public void aEventIsReceivedForACustomer(String arg0) {
        customer = new Customer();
        customer.setCustomerId(UUID.fromString(moneyTransfer2.getCAccountId()));
        Event event = new Event(arg0, new Object[] {customer, correlationId});
        ps.handleAllPaymentsMadeByCustomerReportRequest(event);
    }

    @Then("a {string} event is sent for a customer with matching correlation id")
    public void aEventIsSentWithMatchingCorrelationId(String arg0) {
        expectedReport = new Report();
        expectedReport.setMoneyTransfers(MoneyTransferRepo.getAllPaymentsByCustomer(customer.getAccountId()));
        expectedReport.setTotalAmount(MoneyTransferRepo.getTotalAmountByCustomer(customer.getAccountId()));
        expectedReport.setReportId(correlationId.getId());
        var event = new Event(arg0, new Object[] {expectedReport, correlationId});
        verify(queue).publish(event);
    }

    @When("a {string} event is received for a merchant")
    public void aEventIsReceivedForAMerchant(String arg0) {
        merchant = new Merchant();
        merchant.setMerchantId(UUID.fromString(moneyTransfer2.getMAccountId()));
        Event event = new Event(arg0, new Object[] {merchant, correlationId});
        ps.handleAllPaymentsMadeByMerchantReportRequest(event);
    }

    @Then("a {string} event is sent for a merchant with matching correlation id")
    public void aEventIsSentForAMerchantWithMatchingCorrelationId(String arg0) {
        expectedReport = new Report();
        expectedReport.setMoneyTransfers(MoneyTransferRepo.getAllPaymentsByMerchant(merchant.getAccountId()));
        expectedReport.setTotalAmount(MoneyTransferRepo.getTotalAmountByMerchant(merchant.getAccountId()));
        expectedReport.setReportId(correlationId.getId());
        var event = new Event(arg0, new Object[] {expectedReport, correlationId});
        verify(queue).publish(event);
    }
}
