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
import org.junit.jupiter.api.Assertions;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @Author Lauritz Pepke s191179
 */

public class CustomerServiceSteps {

    MessageQueue queue = mock(MessageQueue.class);
    CustomerBankService cs = new CustomerBankService(queue);
    Customer customer1, customer2, customer3, customer4;
    Customer expected1, expected2, expected3;
    private CorrelationId correlationId1, correlationId2, correlationId3, correlationId4;


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

    @Given("there is a customer with empty merchant id")
    public void thereIsACustomerWithEmptyMerchantId() {
        customer3 = new Customer();
        correlationId3 = CorrelationId.randomId();
        customer3.setAccountId("test300");
        Assertions.assertNull(customer3.getCustomerId());
    }

    @When("a {string} event for a report is received")
    public void aEventForAReportIsReceived(String arg0) {
        customer3.setCustomerId(UUID.randomUUID());
        CustomerRepo.addCustomer(customer3);
        Event event = new Event(arg0, new Object[] {customer3, correlationId3});
        cs.handleReportAllCustomerPaymentsRequest(event);
    }

    @Then("a {string} event is sent for a customer with customer id assigned")
    public void aEventIsSentForACustomerWithCustomerIdAssigned(String arg0) {
        expected3 = new Customer();
        expected3.setAccountId(customer3.getAccountId());
        expected3.setCustomerId(customer3.getCustomerId());
        System.out.println(customer3);
        var event = new Event(arg0, new Object[] {expected3, correlationId3});
        verify(queue).publish(event);
    }

    @And("the customer has an id assigned")
    public void theCustomerHasAnIdAssigned() {
        Assertions.assertNotNull(CustomerRepo.getCustomer(expected3.getCustomerId()));
    }

    @Given("there is a customer registered with non-empty values")
    public void thereIsACustomerRegisteredWithNonEmptyValues() {
        customer4 = new Customer();
        customer4.setCustomerId(UUID.randomUUID());
        customer4.setAccountId("test4");
        correlationId4 = CorrelationId.randomId();
        CustomerRepo.addCustomer(customer4);
        Assertions.assertNotNull(customer4.getCustomerId());
        Assertions.assertNotNull(customer4.getAccountId());
    }

    @When("a {string} event is received for a customer account")
    public void aEventIsReceivedForACustomerAccount(String arg0) {
        Event event = new Event(arg0, new Object[] {customer4.getCustomerId(), correlationId4});
        cs.handleCustomerAccountDelete(event);
    }

    @Then("a {string} event is sent to delete")
    public void aEventIsSentToDelete(String arg0) {
        System.out.println(customer4);
        var event = new Event(arg0, new Object[] {customer4, correlationId4});
        verify(queue).publish(event);
    }

    @And("the customer account is deleted")
    public void theCustomerAccountIsDeleted() {
        Assertions.assertNull(CustomerRepo.getCustomer(customer4.getCustomerId()));
    }
}
