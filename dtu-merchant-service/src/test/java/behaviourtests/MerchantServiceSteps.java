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


import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerchantServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);
    MerchantBankService ms = new MerchantBankService(queue);
    Merchant merchant1;
    Merchant merchant2;
    Merchant expected1;
    Merchant expected2;
    private CorrelationId correlationId1;
    private CorrelationId correlationId2;


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


}
