package e2eTest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MoneyTransferSteps {



    @Given("there is a payment with empty amount, token and mid")
    public void thereIsAPaymentWithEmptyAmountTokenAndMid() {
    }

    @When("the payment is being initiated")
    public void thePaymentIsBeingInitiated() {
    }

    @Then("a {string} event is sent")
    public void aEventIsSent(String arg0) {
    }

    @When("a {string} event for the payment is sent with non-empty amount")
    public void aEventForThePaymentIsSentWithNonEmptyAmount(String arg0) {
    }

    @And("a {string} event for the payment is sent with non-empty mid")
    public void aEventForThePaymentIsSentWithNonEmptyMid(String arg0) {
    }

    @And("a {string} event for the payment is sent with non-empty token")
    public void aEventForThePaymentIsSentWithNonEmptyToken(String arg0) {
    }

    @Then("the payment is set with an amount, a token and a mid")
    public void thePaymentIsSetWithAnAmountATokenAndAMid() {
    }

    @When("the {string} event for a payment is sent")
    public void theEventForAPaymentIsSent(String arg0) {
    }

    @Then("the amount is deducted from the Customer bank account")
    public void theAmountIsDeductedFromTheCustomerBankAccount() {
    }

    @And("the amount is added to the Merchant bank account")
    public void theAmountIsAddedToTheMerchantBankAccount() {
    }
}
