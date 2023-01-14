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

    private final CompletableFuture<Merchant> regMerchantFuture = new CompletableFuture<>();
    private final CompletableFuture<Merchant> getMerchantFuture = new CompletableFuture<>();
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
}
