package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Merchant;
import org.acme.MerchantBankService;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MerchantServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);
    MerchantBankService ms = new MerchantBankService(queue);
    Merchant merchant;
    Merchant expected;
    private CorrelationId correlationId;


    @When("a {string} event for a merchant is received")
    public void aEventForAMerchantIsReceived(String arg0) {
        merchant = new Merchant();
        merchant.setAccountId("test123");
        assertNull(merchant.getMerchantId());
        System.out.println(merchant);
        correlationId = CorrelationId.randomId();
        ms.handleMerchantAccountRegister(new Event(arg0, new Object[] {merchant, correlationId}));
    }

    @Then("the {string} event is sent with the same correlation id")
    public void theEventIsSentWithTheSameCorrelationId(String eventName) {
        expected = new Merchant();
        expected.setAccountId("test123");
        expected.setMerchantId(correlationId.getId());
        System.out.println(expected);
        var event = new Event(eventName, new Object[] {expected, correlationId});
        verify(queue).publish(event);
    }

    @And("the merchant account is registered")
    public void theMerchantAccountIsRegistered() {
        System.out.println(expected.getMerchantId());
        assertNotNull(expected.getMerchantId());
    }
}
