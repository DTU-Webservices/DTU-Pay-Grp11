package org.acme;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.TransferMoneyFromToResponse;
import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Entity.*;
import org.acme.Repo.MoneyTransferRepo;
import org.acme.Repo.PaymentRepo;
import org.acme.Repo.TokenRepo;


import java.math.BigDecimal;
import java.util.UUID;

public class PaymentService {

    private static final String PAYMENT_CREATED = "PaymentCreated";
    private static final String CUSTOMER_REQUEST1 = "GetCustomerIdForTransferReq";
    private static final String CUSTOMER_REQUEST = "GetCustomerAccForTransferReq";
    private static final String MERCHANT_REQUEST = "GetMerchantAccForTransferReq";
    private static final String REPORT_ALL_PAYMENTS_RESP = "ReportAllPayResp";

    private final BankService bankService = new BankServiceService().getBankServicePort();

    MessageQueue queue;

    public PaymentService(MessageQueue q)  {
        this.queue = q;
        this.queue.addHandler("PaymentCreateReq", this::handlePaymentRequested);
        this.queue.addHandler("MerchantAccResponse", this::handleMerchantAccountIdGetReq);
        //this.queue.addHandler("CustomerIdResponse". this::handleCustomerIdGetReq);
        this.queue.addHandler("CustomerAccResponse", this::handleCustomerAccountIdGetReq);
        this.queue.addHandler("ReportAllPayReq", this::handleAllPaymentsReportRequest);

    }

    public void handlePaymentRequested(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);

        createPaymentAndSave(p, correlationId);

        var merchant = new Merchant();
        merchant.setMerchantId(UUID.fromString(p.getMid()));
        handleEventPublish(merchant, MERCHANT_REQUEST, correlationId);
    }

    private void createPaymentAndSave(Payment p, CorrelationId correlationId) {
        p.setPaymentId(correlationId.getId());
        PaymentRepo.addPayment(p);
    }

    public void handleMerchantAccountIdGetReq(Event ev) {
        Merchant merchant = ev.getArgument(0, Merchant.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);
        System.out.println("HALLOO=OOOOOOOOOOOOOQWERTYUIOPASDFGHJKLZXCVBNMOOOOOOOOOOOOOQWERTYUIOPASDFGHJKLZXCVBNM");
        Payment payment = createMoneyTransferAndSave(merchant, correlationId);

        var customer = new Customer();
        customer.setCurrentToken(UUID.fromString(payment.getToken()));
        System.out.println("cid: " + UUID.fromString(payment.getToken()));
        System.out.println("cid: " + UUID.fromString(payment.getToken()));
        System.out.println("cid: " + UUID.fromString(payment.getToken()));
        var cid = TokenRepo.getCidToken(UUID.fromString(payment.getToken()));
        System.out.println("cid: " + cid);
        customer.setCustomerId(cid);
        handleEventPublish(customer, CUSTOMER_REQUEST, correlationId);
    }

    private Payment createMoneyTransferAndSave(Merchant merchant, CorrelationId correlationId) {
        Payment payment = PaymentRepo.getPayment(correlationId.getId());
        MoneyTransfer mt = new MoneyTransfer();
        mt.setMtId(correlationId.getId());
        mt.setAmount(payment.getAmount());
        mt.setMAccountId(merchant.getAccountId());

        MoneyTransferRepo.addMoneyTransfer(mt);

        return payment;
    }
    public void handleCustomerAccountIdGetReq(Event ev) {
        Customer customer = ev.getArgument(0, Customer.class);
        CorrelationId correlationId = ev.getArgument(1, CorrelationId.class);

        MoneyTransfer mt = getMoneyTransferAndUpdateCustomerAccountId(customer, correlationId);
        transferMoneyAtBank(mt);

        handleEventPublish(mt, PAYMENT_CREATED, correlationId);
    };

    private MoneyTransfer getMoneyTransferAndUpdateCustomerAccountId(Customer customer, CorrelationId correlationId) {
        var mt = MoneyTransferRepo.getMoneyTransfer(correlationId.getId());
        mt.setCAccountId(customer.getAccountId());
        MoneyTransferRepo.updateMoneyTransfer(mt);
        return mt;
    }

    private void transferMoneyAtBank(MoneyTransfer mt) {
        try {
            bankService.transferMoneyFromTo(mt.getMAccountId(), mt.getCAccountId(),  new BigDecimal(mt.getAmount()), "Payment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAllPaymentsReportRequest(Event e) {
        var correlationId = e.getArgument(0, CorrelationId.class);
        handleEventPublish(createReportAndImportAllPayments(correlationId), REPORT_ALL_PAYMENTS_RESP, correlationId);
    }
    private Report createReportAndImportAllPayments(CorrelationId correlationId) {
        Report report = new Report();
        report.setMoneyTransfers(MoneyTransferRepo.getAllPayments());
        report.setReportId(correlationId.getId());
        return report;
    }
    private void handleEventPublish(Object object, String event, CorrelationId correlationId) {
        Event e = new Event(event, new Object[] {object, correlationId});
        queue.publish(e);
    }
}