package e2eTest;

import dtu.ws.fastmoney.*;
import e2eTest.Entities.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Kristoffer T. Pedersen s205354.
 */

public class MerchantMakesPaymentSteps {

    private final TestService service = new TestService();

    private final BankService bs = new BankServiceService().getBankServicePort();

    private String MERCHANT_ACCOUNT_ID;
    private String CUSTOMER_ACCOUNT_ID;

    private Merchant merchantHolder;
    private Customer customerHolder;
    private String tokenHolder;

    @Given("Given a user with a cprNumber of {string} and a firstName of {string} and a LastName of {string}")
    public void givenAUserWithACprNumberOfAndAFirstNameOfAndALastNameOf(String arg0, String arg1, String arg2) throws BankServiceException_Exception {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        User user = new User();
        user.setCprNumber(arg0);
        user.setFirstName(arg1);
        user.setLastName(arg2);
        completableFuture.complete(bs.createAccountWithBalance(user, BigDecimal.valueOf(1000)));
        MERCHANT_ACCOUNT_ID = completableFuture.join();
        assertNotNull(MERCHANT_ACCOUNT_ID);
    }

    @And("a user with a cprNumber of {string} and a firstName of {string} and a LastName of {string}")
    public void aUserWithACprNumberOfAndAFirstNameOfAndALastNameOf(String arg0, String arg1, String arg2) throws BankServiceException_Exception {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        User user = new User();
        user.setCprNumber(arg0);
        user.setFirstName(arg1);
        user.setLastName(arg2);
        completableFuture.complete(bs.createAccountWithBalance(user, BigDecimal.valueOf(1000)));
        CUSTOMER_ACCOUNT_ID = completableFuture.join();
        assertNotNull(CUSTOMER_ACCOUNT_ID);
    }

    @When("they are registered with their bank")
    public void theyAreRegisteredWithTheirBank() throws BankServiceException_Exception {
        CompletableFuture<Account> completableFuture1 = new CompletableFuture<>();
        CompletableFuture<Account> completableFuture2 = new CompletableFuture<>();

        completableFuture1.complete(bs.getAccount(MERCHANT_ACCOUNT_ID));
        completableFuture2.complete(bs.getAccount(CUSTOMER_ACCOUNT_ID));
        assertNotNull(completableFuture1.join());
        assertNotNull(completableFuture2.join());
    }

    @Then("Paul can register in DTU Pay as a Merchant with his accountId")
    public void paulCanRegisterInDTUPayAsAMerchantWithHisAccountId() {
        CompletableFuture<Merchant> completableFuture = new CompletableFuture<>();
        Merchant merchant = new Merchant();
        merchant.setAccountId(MERCHANT_ACCOUNT_ID);
        completableFuture.complete(service.registerMerchant(merchant));
        merchantHolder = completableFuture.join();
        assertNotNull(merchantHolder);
    }

    @And("Hanne can register in DTU Pay as a Customer with her accountId")
    public void hanneCanRegisterInDTUPayAsACustomerWithHerAccountId() {
        CompletableFuture<Customer> completableFuture = new CompletableFuture<>();
        Customer customer = new Customer();
        customer.setAccountId(CUSTOMER_ACCOUNT_ID);
        completableFuture.complete(service.registerCustomer(customer));
        customerHolder = completableFuture.join();
        assertNotNull(customerHolder);
    }

    @Then("Hanne can generate {int} tokens")
    public void hanneCanGenerateTokens(int arg0) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Token token = new Token();
        token.setCustomerId(customerHolder.getCustomerId());
        token.setQty(String.valueOf(arg0));
        completableFuture.complete(service.generateTokens(token));
        assertNotNull(completableFuture.join());
    }

    @And("Paul can get a token from Hanne")
    public void paulCanGetATokenFromHanne() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete(service.getToken(customerHolder.getCustomerId()));
        tokenHolder = completableFuture.join();
        assertNotNull(tokenHolder);
    }

    @Then("Paul can make a payment of {int} from Hanne")
    public void paulCanMakeAPaymentOfFromHanne(int arg0) {
        CompletableFuture<MoneyTransfer> completableFuture = new CompletableFuture<>();

        Payment payment = new Payment();
        payment.setMid(merchantHolder.getMerchantId().toString());
        payment.setToken(tokenHolder);
        payment.setAmount(String.valueOf(arg0));

        completableFuture.complete(service.makePaymentAndTransferMoney(payment));
        assertNotNull(completableFuture.join());
    }

    @Then("Hanne can see her balance of {int}")
    public void hanneCanSeeHerBalanceOf(int arg0) throws BankServiceException_Exception {
        CompletableFuture<Account> completableFuture = new CompletableFuture<>();

        completableFuture.complete(bs.getAccount(customerHolder.getAccountId()));
        assertEquals(completableFuture.join().getBalance(), BigDecimal.valueOf(arg0));
    }

    @Then("Paul can see his balance of {int}")
    public void paulCanSeeHisBalanceOf(int arg0) throws BankServiceException_Exception {
        CompletableFuture<Account> completableFuture = new CompletableFuture<>();

        completableFuture.complete(bs.getAccount(merchantHolder.getAccountId()));
        assertEquals(completableFuture.join().getBalance(), BigDecimal.valueOf(arg0));
    }

    @After("@MerchantMakesPayment")
    public  void deleteAfterTest() throws BankServiceException_Exception {
        System.out.println("Deleting Paul");
        bs.retireAccount(MERCHANT_ACCOUNT_ID);
        System.out.println("Deleting Hanne");
        bs.retireAccount(CUSTOMER_ACCOUNT_ID);
    }
}
