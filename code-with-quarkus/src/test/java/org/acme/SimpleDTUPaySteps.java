package org.acme;

import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleDTUPaySteps {
    String cid, mid;
    PaymentService dtuPay = new PaymentService();
    AccountService service = new AccountService();
    Payment payment = new Payment("10","cid1","mid1");
    String accountID;
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
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        successful = true;
    }


    @Given("a successful payment of {string} kr from customer {string} to merchant {string}")
    public void aSuccessfulPaymentOfKrFromCustomerToMerchant(String arg0, String arg1, String arg2) {
        Payment payment = new Payment(arg0, arg1, arg2);
        dtuPay.pay(payment);
    }

    @When("the manager asks for at list of payments")
    public void theManagerAsksForAtListOfPayments() {
        Assertions.assertNotNull(dtuPay.getPayments());
    }

    @Then("the list contains a list of payments where customer {string} paid {string} kr to merchant {string}")
    public void theListContainsAListOfPaymentsWhereCustomerPaidKrToMerchant(String arg0, String arg1, String arg2) {
        Set<Payment> payments = dtuPay.getPayments();
        boolean found = false;
        for (Payment p : payments) {
            if (p.getCid().equals(arg0) && p.getMid().equals(arg2) && p.getAmount().equals(arg1)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Then("the payment is not successful")
    public void thePaymentIsNotSuccessful() {
        assertFalse(successful);
    }

    @And("an error message is returned saying {string}")
    public void anErrorMessageIsReturnedSaying(String arg0) {
        payment = new Payment("10","cid1","mid2");
        dtuPay.pay(payment);
    }

    @Given("a customer with a bank account with balance {int}")
    public void aCustomerWithABankAccountWithBalance(int arg0) throws BankServiceException_Exception {
        try {
            User user = new User();
            user.setCprNumber("888844-4444");
            user.setFirstName("Ted");
            user.setLastName("Banks");
            accountID = dtuPay.createBankAccount(user, new BigDecimal(arg0));
            if (accountID != null) {
                assertTrue(true);
                cid = accountID;
            }
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }

    }

    @And("that the customer is registered with DTU Pay")
    public void thatTheCustomerIsRegisteredWithDTUPay() {

    }

    @Given("a merchant with a bank account with balance {int}")
    public void aMerchantWithABankAccountWithBalance(int arg0) throws BankServiceException_Exception {

        try {
            User user = new User();
            user.setCprNumber("654321-1234");
            user.setFirstName("Rob");
            user.setLastName("Tanks");
            accountID = dtuPay.createBankAccount(user, new BigDecimal(arg0));
            if (accountID != null) {
                assertTrue(true);
                mid = accountID;
            }
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

    @And("that the merchant is registered with DTU Pay")
    public void thatTheMerchantIsRegisteredWithDTUPay() {
    }

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int arg0) {
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int arg0) {
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int arg0) {
    }

    @Then("delete all accounts")
    public void deleteAllAccounts() throws BankServiceException_Exception {
        dtuPay.retireAccount(cid);
        dtuPay.retireAccount(mid);
    }

    @After
    public void tearDownBankTest() throws BankServiceException_Exception {
        service.retireRegisteredAccounts();
    }


}
