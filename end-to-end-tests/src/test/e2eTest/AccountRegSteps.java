package e2eTest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AccountRegSteps {

    RegService service = new RegService();

    private Merchant merchant;
    private Customer customer;

    private final CompletableFuture<Merchant> regMerchantFuture = new CompletableFuture<>();
    private final CompletableFuture<Merchant> getMerchantFuture = new CompletableFuture<>();
    private final CompletableFuture<Customer> regCustomerFuture = new CompletableFuture<>();
    private final CompletableFuture<Customer> getCustomerFuture = new CompletableFuture<>();
    @Given("an unregistered merchant with a valid accountId: {string}")
    public void anUnregisteredMerchantWithAValidAccountId(String arg0) {
        //Something here to test with bank API
        merchant = new Merchant();
        merchant.setAccountId(arg0);
        assertNull(merchant.getMerchantId());
    }

    @When("When the merchant registers with a valid accountId: {string}")
    public void whenTheMerchantRegistersWithAValidAccountId(String arg0) {
        regMerchantFuture.complete(service.registerMerchant(merchant));
    }

    @Then("the merchant is registered")
    public void theMerchantIsRegistered() {
        merchant = regMerchantFuture.join();
        assertNotNull(merchant);
    }

    @And("the merchant can be retrieved by merchantId")
    public void theMerchantCanBeRetrievedBy() {
        getMerchantFuture.complete(service.getMerchant(merchant.getMerchantId()));
        System.out.println(merchant);
        System.out.println(service.getMerchant(merchant.getMerchantId()));
        assertNotNull(getMerchantFuture.join().getAccountId());
    }

    @Given("an unregistered customer with a valid accountId: {string}")
    public void anUnregisteredCustomerWithAValidAccountId(String arg0) {
        //Something here to test with bank API
        customer = new Customer();
        customer.setAccountId(arg0);
        assertNull(customer.getCustomerId());
    }

    @When("When the customer registers with a valid accountId: {string}")
    public void whenTheCustomerRegistersWithAValidAccountId(String arg0) {
        regCustomerFuture.complete(service.registerCustomer(customer));
    }

    @Then("the customer is registered")
    public void theCustomerIsRegistered() {
        customer = regCustomerFuture.join();
        assertNotNull(customer);
    }

    @And("the customer can be retrieved by customerId")
    public void theCustomerCanBeRetrievedBy() {
        getCustomerFuture.complete(service.getCustomer(customer.getCustomerId()));
        System.out.println(customer);
        System.out.println(service.getCustomer(customer.getCustomerId()));
        assertNotNull(getCustomerFuture.join().getAccountId());
    }
}
