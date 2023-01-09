package org.acme;

import java.util.*;

public class CustomerService {

    //private final Set<Customer> customers = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    private final HashMap<String, Customer> registeredUser = new HashMap<String, Customer>();

    public CustomerService() {
        System.out.println("CustomerService Created");
    }

    public HashMap<String, Customer> getCustomers() {
        return registeredUser;
    }

    public void addCustomer(String cpr, Customer c) {
        registeredUser.put(cpr, c);
    }

    public void removeCustomer(String cpr) {
        registeredUser.remove(cpr);
    }

    public String getCustomer(String cpr) {
        String notFound = "Customer Not Found";
        Customer result = registeredUser.get(cpr);
        if (cpr != null) {
            return result.toString();
        } else {
            return notFound;
        }
    }


}
