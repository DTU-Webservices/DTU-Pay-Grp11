package e2eTest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MerchantMakesPaymentSteps {
    @Given("Given a user with a cprNumber of {string} and a firstName of {string} and a LastName of {string}")
    public void givenAUserWithACprNumberOfAndAFirstNameOfAndALastNameOf(String arg0, String arg1, String arg2) {
        throw new io.cucumber.java.PendingException();
    }

    @And("a user with a cprNumber of {string} and a firstName of {string} and a LastName of {string}")
    public void aUserWithACprNumberOfAndAFirstNameOfAndALastNameOf(String arg0, String arg1, String arg2) {
        throw new io.cucumber.java.PendingException();
    }

    @When("they are registered with their bank")
    public void theyAreRegisteredWithTheirBank() {
        throw new io.cucumber.java.PendingException();
    }

    @Then("Paul can register in DTU Pay as a Merchant with his accountId with a balance of {int}")
    public void paulCanRegisterInDTUPayAsAMerchantWithHisAccountIdWithABalanceOf(int arg0) {
        throw new io.cucumber.java.PendingException();
    }

    @And("Hanne can register in DTU Pay as a Customer with her accountId with a balance of {int}")
    public void hanneCanRegisterInDTUPayAsACustomerWithHerAccountIdWithABalanceOf(int arg0) {
        throw new io.cucumber.java.PendingException();
    }

    @Then("Hanne can generate {int} tokens")
    public void hanneCanGenerateTokens(int arg0) {
        throw new io.cucumber.java.PendingException();
    }

    @And("Paul can get a token from Hanne")
    public void paulCanGetATokenFromHanne() {
        throw new io.cucumber.java.PendingException();
    }

    @Then("Paul can make a payment of {int} from Hanne")
    public void paulCanMakeAPaymentOfFromHanne(int arg0) {
        throw new io.cucumber.java.PendingException();
    }

    @Then("Hanne can see her balance of {int}")
    public void hanneCanSeeHerBalanceOf(int arg0) {
        throw new io.cucumber.java.PendingException();
    }

    @Then("Paul can see his balance of {int}")
    public void paulCanSeeHisBalanceOf(int arg0) {
        throw new io.cucumber.java.PendingException();
    }
}
