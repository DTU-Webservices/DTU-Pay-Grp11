package org.acme;

import dtu.ws.fastmoney.*;
import org.json.XML;

import java.math.BigDecimal;
import java.util.*;

public class PaymentService {

    BankService bank = new BankServiceService().getBankServicePort();

    private final Set<Payment> payments = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    private final Set<Customer> customers = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    private final Set<Merchant> merchants = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    private User user = new User();
    private boolean isUserSet = false;

    public PaymentService() {
        System.out.println("PaymentService Created");
    }



    public String createBankAccount(User user, BigDecimal balance) throws BankServiceException_Exception {
        String accountId = bank.createAccountWithBalance(user, balance);
        System.out.println("AccountID: " + accountId);
        return accountId;
    }

    public void retireAccount(String accountId) throws BankServiceException_Exception {
        try {
            bank.retireAccount(accountId);
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

    public void transferMoney(String from, String to, BigDecimal amount) throws BankServiceException_Exception {
        bank.transferMoneyFromTo(from, to, amount, "Transfer");
    }

    public List<AccountInfo> getAccounts() throws BankServiceException_Exception {
    	return bank.getAccounts();
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

    public void addCustomer(Customer c) {
    	customers.add(c);
    }
    public Customer getCustomer(String cid) {
    	for (Customer c : customers) {
    		if (c.getCid().equals(cid)) {
    			return c;
    		}
    	}
    	return null;
    }










}
