package org.acme;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;

import java.util.*;

public class AccountService {

    BankService bank = new BankServiceService().getBankServicePort();

    //private final Set<Customer> customers = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private final HashMap<String, Account> registeredUser = new HashMap<String, Account>();

    public AccountService() {
        System.out.println("CustomerService Created");
    }

    public HashMap<String, Account> getCustomers() {
        return registeredUser;
    }

    public void addCustomer(String cpr, Account c) {
        registeredUser.put(cpr, c);
    }

    public void removeCustomer(String cpr) {
        registeredUser.remove(cpr);
    }

    public String getCustomer(String cpr) {
        String notFound = "Customer Not Found";
        Account result = registeredUser.get(cpr);
        if (result != null) {
            return result.toString();
        } else {
            return notFound;
        }
    }

    public String getAccountId(String cpr) {
        String notFound = "Customer Not Found";
        String result = registeredUser.get(cpr).getBankAddress();
        if (cpr != null) {
            return result;
        } else {
            return notFound;
        }
    }

    public void retireRegisteredAccounts() throws BankServiceException_Exception {
        try {
            for (Map.Entry<String, Account> set :
            registeredUser.entrySet()) {
                bank.retireAccount(set.getValue().getBankAddress());
            }
            registeredUser.clear();
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
        }
    }

}
