package org.acme;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

public class PaymentService {

    BankService bank = new BankServiceService().getBankServicePort();

    private final Set<Payment> payments = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    private final Set<Customer> customers = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    private final Set<Merchant> merchants = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    private User user = new User();
    private boolean isUserSet = false;

    public PaymentService() {
        payments.add(new Payment("100", "1234567890", "1234567890"));
        payments.add(new Payment("100", "123456227890", "1234567890"));
        payments.add(new Payment("100", "123456222227890", "1234562227890"));
        System.out.println("PaymentService Created");
    }



    public String createBankAccount(User user, BigDecimal balance) throws BankServiceException_Exception {
        return bank.createAccountWithBalance(user, balance);
    }

    public void retireAccount(String accountId) throws BankServiceException_Exception {
    	bank.retireAccount(accountId);
    }

    public void transferMoney(String from, String to, BigDecimal amount) throws BankServiceException_Exception {
        bank.transferMoneyFromTo(from, to, amount, "Transfer");
    }



    public Payment getPayment() {
        return payments.iterator().next();
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void pay(Payment p) {
        payments.add(p);
    }


    public String getCid(Payment p) {
    	return p.getCid();
    }

    public String getMid(Payment p) {
    	return p.getMid();
    }










}
