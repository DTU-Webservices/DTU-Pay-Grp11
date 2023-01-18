package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Customer;
import org.acme.CustomerBankService;
import org.acme.Repo.CustomerRepo;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CustomerServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);
    CustomerBankService cs = new CustomerBankService(queue);
    Customer customer1;
    Customer customer2;
    Customer expected1;
    Customer expected2;
    private CorrelationId correlationId1;
    private CorrelationId correlationId2;




    @When("a {string} event for a customer is received")
    public void aEventForACustomerIsReceived(String arg0) {
        customer1 = new Customer();
        customer1.setAccountId("test1");
        assertNull(customer1.getCustomerId());
        correlationId1 = CorrelationId.randomId();
        cs.handleCustomerAccountRegister(new Event(arg0, new Object[] {customer1, correlationId1}));
    }

    @Then("the {string} event is sent with the same correlation id")
    public void theEventIsSentWithTheSameCorrelationId(String arg0) {
        expected1 = new Customer();
        expected1.setAccountId("test1");
        expected1.setCustomerId(correlationId1.getId());
        System.out.println(expected1);
        var event = new Event(arg0, new Object[] {expected1, correlationId1});
        verify(queue).publish(event);
    }

    @And("the customer account is registered")
    public void theCustomerAccountIsRegistered() {
        System.out.println(expected1.getCustomerId());
        assertNotNull(expected1.getCustomerId());
    }

    @Given("there is a customer registered")
    public void thereIsACustomerRegistered() {
        customer2 = new Customer();
        customer2.setAccountId("test2");
        correlationId2 = CorrelationId.randomId();
        customer2.setCustomerId(correlationId2.getId());
        CustomerRepo.addCustomer(customer2);
        assertNotNull(CustomerRepo.getCustomer(customer2.getCustomerId()));
    }

    @When("a {string} event for a transfer is received")
    public void aEventForATransferIsReceived(String arg0) {
        Event event = new Event(arg0, new Object[] {customer2, correlationId2});
        cs.handleCustomerAccountGetForTransfer(event);
    }

    @Then("a {string} event for a transfer is sent with the same correlation id")
    public void aEventForATransferIsSentWithTheSameCorrelationId(String arg0) {
        expected2 = new Customer();
        expected2.setAccountId("test2");
        expected2.setCustomerId(correlationId2.getId());
        var event = new Event(arg0, new Object[] {expected2, correlationId2});
        verify(queue).publish(event);
    }

    @And("the customer for a transfer is retrieved")
    public void theCustomerForATransferIsRetrieved() {
        assertNotNull(CustomerRepo.getCustomer(customer2.getCustomerId()));
    }
}
