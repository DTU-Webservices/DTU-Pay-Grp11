package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomerServiceSteps {
    @When("a {string} event is received")
    public void aEventIsReceived(String arg0) {
    }

    @Then("a {string} event is sent")
    public void aEventIsSent(String arg0) {
    }

    @Then("the {string} event is sent with non-empty id")
    public void theEventIsSentWithNonEmptyId(String arg0) {
    }

    @And("the payment gets a merchant bank account id")
    public void thePaymentGetsAMerchantBankAccountId() {
    }
}
