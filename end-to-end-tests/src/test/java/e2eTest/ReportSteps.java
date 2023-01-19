package e2eTest;

import e2eTest.Entities.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Kristoffer T. Pedersen s205354.
 */

public class ReportSteps {

    /**
     * Important that this is being run after the MerchantPayment for data to be present in all data.
     *
     * This will cause the payment flow to throw an error during the bankService money transfer. This is okay for this test.
     */

    private UUID generatedMerchantId;
    private UUID generatedCustomerId;

    private TestService service = new TestService();

    private final CompletableFuture<Report> allPaymentsFuture = new CompletableFuture<>();
    private final CompletableFuture<Report> customerReportFuture = new CompletableFuture<>();
    private final CompletableFuture<Report> merchantReportFuture = new CompletableFuture<>();

    @Before("@Report")
    public void initialPayment() {
        //Fabricating payment
        Payment payment = new Payment();
        payment.setAmount("100");

        Merchant merchant = new Merchant();
        String MERCHANT_TEST_ACCOUNT_ID = "TEST_MERCHANT_ACCOUNT_ID";
        merchant.setAccountId(MERCHANT_TEST_ACCOUNT_ID);
        CompletableFuture<Merchant> merchantCompletableFuture = new CompletableFuture<>();
        merchantCompletableFuture.complete(service.registerMerchant(merchant));
        merchant = merchantCompletableFuture.join();
        payment.setMid(merchant.getMerchantId().toString());
        generatedMerchantId = merchant.getMerchantId();

        Customer customer = new Customer();
        String CUSTOMER_TEST_ACCOUNT_ID = "TEST_CUSTOMER_ACCOUNT_ID";
        customer.setAccountId(CUSTOMER_TEST_ACCOUNT_ID);
        CompletableFuture<Customer> customerFuture = new CompletableFuture<>();
        customerFuture.complete(service.registerCustomer(customer));
        customer = customerFuture.join();
        generatedCustomerId = customer.getCustomerId();

        CompletableFuture<String> genToken = new CompletableFuture<>();
        Token token = new Token();
        token.setCustomerId(customer.getCustomerId());
        token.setQty("1");

        genToken.complete(service.generateTokens(token));
        CompletableFuture<String> getTokenFuture = new CompletableFuture<>();
        getTokenFuture.complete(service.getToken(customer.getCustomerId()));
        payment.setToken(getTokenFuture.join());

        CompletableFuture<MoneyTransfer> moneyTransferCompletableFuture = new CompletableFuture<>();
        moneyTransferCompletableFuture.complete(service.makePaymentAndTransferMoney(payment));
        System.out.println(moneyTransferCompletableFuture.join());
    }
    @Given("any manager that wants a report of all payments")
    public void anyManagerThatWantsAReportOfAllPayments() {
        allPaymentsFuture.complete(service.getAllPaymentsForManager());
        assertNotNull(allPaymentsFuture.join());
    }

    @Then("the manager has a report of all payments")
    public void theManagerHasAReportOfAllPayments() {
        Report report = allPaymentsFuture.join();
        assertNotNull(report.getMoneyTransfers());
    }

    @Given("a customer that wants a report of all their payments")
    public void aCustomerThatWantsAReportOfAllTheirPayments() {
        customerReportFuture.complete(service.getAllPaymentsMadeByCustomer(generatedCustomerId));
        assertNotNull(customerReportFuture.join());
    }

    @Then("the customer requests a report getting all their payments")
    public void theCustomerRequestsAReportGettingAllTheirPayments() {
        assertNotNull(customerReportFuture.join().getMoneyTransfers());
    }

    @Given("a merchant that wants a report of all their payments")
    public void aMerchantThatWantsAReportOfAllTheirPayments() {
        merchantReportFuture.complete(service.getAllPaymentsMadeByMerchant(generatedMerchantId));
        assertNotNull(merchantReportFuture.join());
    }

    @Then("the merchant requests a report getting all their payments")
    public void theMerchantRequestsAReportGettingAllTheirPayments() {
        assertNotNull(merchantReportFuture.join().getMoneyTransfers());
    }
}
