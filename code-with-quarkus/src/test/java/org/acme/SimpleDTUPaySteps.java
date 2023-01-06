package org.acme;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleDTUPaySteps {
    String cid, mid;
    PaymentService dtuPay = new PaymentService();
    Payment payment = new Payment("10","123","456");
    boolean successful;

    // Scenario: Customer pays merchant
    @Given("a customer with id {string}")
    public void aCustomerWithId(String arg0) {
        this.cid = cid;
    }

    @And("a merchant with id {string}")
    public void aMerchantWithId(String arg0) {
        this.mid = mid;
    }

    @When("the merchant initiates a payment for {string} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(String arg0) {
        payment = new Payment(arg0, cid, mid);
        successful = dtuPay.validatePayment(payment);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
    }


    @Given("a successful payment of {string} kr from customer {string} to merchant {string}")
    public void aSuccessfulPaymentOfKrFromCustomerToMerchant(String arg0, String arg1, String arg2) {
        Payment payment = new Payment(arg0, arg1, arg2);
        dtuPay.pay(payment);
    }

    @When("the manager asks for at list of payments")
    public void theManagerAsksForAtListOfPayments() {
    }

    @Then("the list contains a list of payments where customer {string} paid {string} kr to merchant {string}")
    public void theListContainsAListOfPaymentsWhereCustomerPaidKrToMerchant(String arg0, String arg1, String arg2) {
    }

    @Then("the payment is not successful")
    public void thePaymentIsNotSuccessful() {
    }

    @And("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String arg0) {
    }

}
