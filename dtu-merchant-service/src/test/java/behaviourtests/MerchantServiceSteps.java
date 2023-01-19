package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Merchant;
import org.acme.MerchantBankService;
import org.acme.Repo.MerchantRepo;


import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerchantServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);
    MerchantBankService ms = new MerchantBankService(queue);
    Merchant merchant1;
    Merchant merchant2;
    Merchant merchant3;
    Merchant merchant4;
    Merchant expected1;
    Merchant expected2;
    Merchant expected3;
    private CorrelationId correlationId1;
    private CorrelationId correlationId2;
    private CorrelationId correlationId3;
    private CorrelationId correlationId4;


    @When("a {string} event for a merchant is received")
    public void aEventForAMerchantIsReceived(String arg0) {
        merchant1 = new Merchant();
        merchant1.setAccountId("test123");
        assertNull(merchant1.getMerchantId());
        System.out.println(merchant1);
        correlationId1 = CorrelationId.randomId();
        ms.handleMerchantAccountRegister(new Event(arg0, new Object[] {merchant1, correlationId1}));
    }

    @Then("the {string} event is sent with the same correlation id")
    public void theEventIsSentWithTheSameCorrelationId(String arg0) {
        expected1 = new Merchant();
        expected1.setAccountId("test123");
        expected1.setMerchantId(correlationId1.getId());
        System.out.println(expected1);
        var event = new Event(arg0, new Object[] {expected1, correlationId1});
        verify(queue).publish(event);
    }

    @And("the merchant account is registered")
    public void theMerchantAccountIsRegistered() {
        System.out.println(expected1.getMerchantId());
        assertNotNull(expected1.getMerchantId());
    }

    @Given("there is a merchant registered")
    public void thereIsAMerchantRegistered() {
        merchant2 = new Merchant();
        merchant2.setAccountId("test200");
        correlationId2 = CorrelationId.randomId();
        merchant2.setMerchantId(correlationId2.getId());
        MerchantRepo.addMerchant(merchant2);
        assertNotNull(MerchantRepo.getMerchant(merchant2.getMerchantId()));
    }

    @When("a {string} event for a transfer is received")
    public void aEventIsReceived(String arg0) {
        Event event = new Event(arg0, new Object[] {merchant2, correlationId2});
        ms.handleMerchantAccountGetForTransfer(event);
    }

    @Then("a {string} event for a transfer is sent with the same correlation id")
    public void aEventForATransferIsSentWithTheSameCorrelationId(String arg0) {
        expected2 = new Merchant();
        expected2.setAccountId("test200");
        expected2.setMerchantId(correlationId2.getId());
        var event = new Event(arg0, new Object[] {expected2, correlationId2});
        verify(queue).publish(event);
    }

    @And("the merchant for a transfer is retrieved")
    public void theMerchantForATransferIsRetrieved() {
        assertNotNull(MerchantRepo.getMerchant(merchant2.getMerchantId()));
    }


    @Given("there is a merchant with empty merchant id")
    public void thereIsAMerchantWithEmptyMerchantId() {
        merchant3 = new Merchant();
        correlationId3 = CorrelationId.randomId();
        merchant3.setAccountId("test300");
        assertNull(merchant3.getMerchantId());
    }

    @When("a {string} event for a report is received")
    public void aEventForAReportIsReceived(String arg0) {
        //add merchant to repo as a simulation
        merchant3.setMerchantId(UUID.randomUUID());
        MerchantRepo.addMerchant(merchant3);
        Event event = new Event(arg0, new Object[] {merchant3, correlationId3});
        ms.handleReportAllMerchantPaymentsRequest(event);
    }

    @Then("a {string} event is sent for a merchant with merchant id assigned")
    public void aEventIsSentWithMerchantId(String arg0) {
        expected3 = new Merchant();
        expected3.setAccountId(merchant3.getAccountId());
        expected3.setMerchantId(merchant3.getMerchantId());
        System.out.println(merchant3);
        var event = new Event(arg0, new Object[] {expected3, correlationId3});
        verify(queue).publish(event);
    }

    @And("the merchant has an id assigned")
    public void theMerchantHasAnIdAssigned() {
        assertNotNull(MerchantRepo.getMerchant(expected3.getMerchantId()));
    }

    @Given("there is a merchant registered with non-empty values")
    public void thereIsAMerchantRegisteredWithNonEmptyValues() {
        merchant4 = new Merchant();
        merchant4.setMerchantId(UUID.randomUUID());
        merchant4.setAccountId("test4");
        correlationId4 = CorrelationId.randomId();
        MerchantRepo.addMerchant(merchant4);
        assertNotNull(merchant4.getMerchantId());
        assertNotNull(merchant4.getAccountId());
    }

    @When("a {string} event is received for a merchant account")
    public void aEventIsReceivedForAMerchantAccount(String arg0) {
        Event event = new Event(arg0, new Object[] {merchant4.getMerchantId(), correlationId4});
        ms.handleMerchantAccountDelete(event);
    }

    @Then("a {string} event is sent to delete")
    public void aEventIsSentToDelete(String arg0) {
        System.out.println(merchant4);
        var event = new Event(arg0, new Object[] {merchant4, correlationId4});
        verify(queue).publish(event);
    }

    @And("the merchant account is deleted")
    public void theMerchantAccountIsDeleted() {
        assertNull(MerchantRepo.getMerchant(merchant4.getMerchantId()));
    }
}
