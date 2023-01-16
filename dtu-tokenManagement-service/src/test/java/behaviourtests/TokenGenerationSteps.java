package behaviourtests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class TokenGenerationSteps {
    @When("a {string} event is received")
    public void aEventIsReceived(String string) {
    }

    @Then("a {string} event is sent")
    public void aEventIsSent(String arg0) {
    }

    @And("the token is assigned to the customer")
    public void theTokenIsAssignedToTheCustomer() {
    }
}
