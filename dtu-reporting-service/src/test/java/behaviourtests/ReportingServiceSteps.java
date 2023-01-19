package behaviourtests;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.MoneyTransfer;
import org.acme.Entity.Report;
import org.acme.ReportingService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;



public class ReportingServiceSteps {
    private static HashMap<UUID, MoneyTransfer> moneyTransferMap = new HashMap<>();

    @Before("ReportingService")
    public void initialization() {
        moneyTransferMap = new HashMap<>();
        moneyTransfer1 = new MoneyTransfer();
        moneyTransfer1.setAmount("100");
        moneyTransfer1.setCAccountId("12345678");
        moneyTransfer1.setMAccountId("87654321");
        moneyTransfer1.setMtId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
        moneyTransfer1.setDescription("test");
        moneyTransferMap.put(moneyTransfer1.getMtId(), moneyTransfer1);
        moneyTransfer2 = new MoneyTransfer();
        moneyTransfer2.setAmount("200");
        moneyTransfer2.setCAccountId("12345678");
        moneyTransfer2.setMAccountId("87654321");
        moneyTransfer2.setMtId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
        moneyTransfer2.setDescription("test");
        moneyTransferMap.put(moneyTransfer2.getMtId(), moneyTransfer2);
        moneyTransfer3 = new MoneyTransfer();
        moneyTransfer3.setAmount("300");
        moneyTransfer3.setCAccountId("12345678");
        moneyTransfer3.setMAccountId("87654321");
        moneyTransfer3.setMtId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
        moneyTransfer3.setDescription("test");
        moneyTransferMap.put(moneyTransfer3.getMtId(), moneyTransfer3);
    }

    MessageQueue queue = mock(MessageQueue.class);

    ReportingService rs = new ReportingService(queue);
    MoneyTransfer moneyTransfer1;
    MoneyTransfer moneyTransfer2;
    MoneyTransfer moneyTransfer3;
    Report report1;
    Report report2;
    Report report3;
    MoneyTransfer expected1;
    MoneyTransfer expected2;
    MoneyTransfer expected3;
    Report expectedReport1;
    Report expectedReport2;
    Report expectedReport3;
    private CorrelationId correlationId1;
    private CorrelationId correlationId2;
    private CorrelationId correlationId3;



    @Given("there is one or more payments")
    public void thereIsOneOrMorePayments() {
        Set<MoneyTransfer> allPayments = new HashSet<>(moneyTransferMap.values());
        correlationId1 = CorrelationId.randomId();
        report1 = new Report();
        report1.setMoneyTransfers(allPayments);
        BigDecimal totalAmount = moneyTransferMap.values().stream().map(MoneyTransfer::getAmount).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        report1.setTotalAmount(totalAmount);
        report1.setReportId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
    }

    @When("a {string} event for All Payments is received")
    public void aEventForAllPaymentsIsReceived(String string) {
        Event event = new Event(string, new Object[]{report1, correlationId1});
        rs.handleAllPaymentsReport(event);
    }

    @Then("a {string} event is sent with the same correlation id")
    public void aEventIsSentWithTheSameCorrelationId(String string) {
        expectedReport1 = new Report();
        Set<MoneyTransfer> allPayments = new HashSet<>(moneyTransferMap.values());
        expectedReport1.setMoneyTransfers(allPayments);
        BigDecimal totalAmount = moneyTransferMap.values().stream().map(MoneyTransfer::getAmount).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        expectedReport1.setTotalAmount(totalAmount);
        expectedReport1.setReportId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
        var event = new Event(string, new Object[]{expectedReport1, correlationId1});
        verify(queue).publish(event);
    }

    @And("the report is generated")
    public void theReportIsGenerated() {
        assertNotNull(report1);
    }

    @Given("there is one or more payments made by customer")
    public void thereIsOneOrMorePaymentsMadeByCustomer() {
        Set<MoneyTransfer> allPayments = new HashSet<>(moneyTransferMap.values());
        correlationId1 = CorrelationId.randomId();
        report2 = new Report();
        report2.setMoneyTransfers(allPayments);
        BigDecimal totalAmount = moneyTransferMap.values().stream().map(MoneyTransfer::getAmount).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        report2.setTotalAmount(totalAmount);
        report2.setReportId(UUID.fromString("a443aa85-6fa8-4b4a-9719-c61929d8acdf"));
    }

    @When("a {string} event for All Payments Made By customer is received")
    public void aEventForAllPaymentsMadeByCustomerIsReceived(String string) {
        Event event = new Event(string, new Object[]{report2, correlationId1});
        rs.handleAllPaymentsMadeByCustomerReport(event);
    }

    @Then("a {string} event is sent with the same correlationId and customer report")
    public void aEventIsSentWithTheSameCorrelationIdAndCustomerReport(String string) {
        expectedReport2 = new Report();
        Set<MoneyTransfer> allPayments = new HashSet<>(moneyTransferMap.values());
        expectedReport2.setMoneyTransfers(allPayments);
        BigDecimal totalAmount = moneyTransferMap.values().stream().map(MoneyTransfer::getAmount).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        expectedReport2.setTotalAmount(totalAmount);
        expectedReport2.setReportId(UUID.fromString("a443aa85-6fa8-4b4a-9719-c61929d8acdf"));
        var event = new Event(string, new Object[]{expectedReport2, correlationId1});
        verify(queue).publish(event);
    }

    @And("the report for customer is generated")
    public void theReportForCustomerIsGenerated() {
        assertNotNull(report2);
    }

    @Given("there is one or more payments made by merchant")
    public void thereIsOneOrMorePaymentsMadeByMerchant() {
        Set<MoneyTransfer> allPayments = new HashSet<>(moneyTransferMap.values());
        correlationId1 = CorrelationId.randomId();
        report3 = new Report();
        report3.setMoneyTransfers(allPayments);
        BigDecimal totalAmount = moneyTransferMap.values().stream().map(MoneyTransfer::getAmount).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        report3.setTotalAmount(totalAmount);
        report3.setReportId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
    }

    @When("a {string} event for All Payments Made By merchant is received")
    public void aEventForAllPaymentsMadeByMerchantIsReceived(String string) {
        Event event = new Event(string, new Object[]{report3, correlationId1});
        rs.handleAllPaymentsMadeByMerchantReport(event);
    }

    @Then("a {string} event is sent with the same correlationId and merchant report")
    public void aEventIsSentWithTheSameCorrelationIdAndMerchantReport(String arg0) {
        expectedReport3 = new Report();
        Set<MoneyTransfer> allPayments = new HashSet<>(moneyTransferMap.values());
        expectedReport3.setMoneyTransfers(allPayments);
        BigDecimal totalAmount = moneyTransferMap.values().stream().map(MoneyTransfer::getAmount).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
        expectedReport3.setTotalAmount(totalAmount);
        expectedReport3.setReportId(UUID.fromString("d443aa85-6fa8-4b4a-9719-c61929d8acdf"));
        var event = new Event(arg0, new Object[]{expectedReport3, correlationId1});
        verify(queue).publish(event);
    }

    @And("the report for merchant is generated")
    public void theReportForMerchantIsGenerated() {
        assertNotNull(report3);
    }
}
