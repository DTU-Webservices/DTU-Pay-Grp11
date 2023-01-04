import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.acme.SimpleDTUPay;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleDTUPaySteps {
    String cid, mid;
    SimpleDTUPay dtuPay = new SimpleDTUPay();
    boolean successful;

    @Given("a customer with id {string}")
    public void aCustomerWithId(String arg0) {
        this.cid = cid;
    }

    @And("a merchant with id {string}")
    public void aMerchantWithId(String arg0) {
        this.mid = mid;
    }

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        successful = dtuPay.pay(amount, cid, mid);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertTrue(successful);
    }

    @Given("a successful payment of {string} kr from customer {string} to merchant {string}")
    public void aSuccessfulPaymentOfKrFromCustomerToMerchant(String arg0, String arg1, String arg2) {
    }

    @When("the manager asks for at list of payments")
    public void theManagerAsksForAtListOfPayments() {
    }

    @Then("the list contains a list of payments where customer {string} paid {string} kr to merchant {string}")
    public void theListContainsAListOfPaymentsWhereCustomerPaidKrToMerchant(String arg0, String arg1, String arg2) {
    }

    @When("the merchant initiates a payment for {string} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(String arg0) {
    }

    @Then("the payment is not successful")
    public void thePaymentIsNotSuccessful() {
    }

    @And("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String arg0) {
    }
}
