package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MerchantServiceSteps {
    @When("a {string} event for a payment is received")
    public void aEventForAPaymentIsReceived(String arg0) {
    }

    @Then("the {string} event is sent")
    public void theEventIsSent(String arg0) {
    }

    @And("the payment gets a merchant bank account id")
    public void thePaymentGetsAMerchantBankAccountId() {
    }

    @And("the payment gets an amount assigned")
    public void thePaymentGetsAnAmountAssigned() {
    }
}
