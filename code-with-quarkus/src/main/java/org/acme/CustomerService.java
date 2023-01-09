package org.acme;

import java.util.*;

public class CustomerService {

    private final Set<Customer> customers = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public CustomerService() {
        System.out.println("CustomerService Created");
    }

/*
    public Customer getCustomer() {
        return customers.iterator().next();
    }

 */


    public Set<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public void removeCustomer(Customer c) {
        customers.remove(c);
    }

    public void removeCustomerByCpr(String cpr) {
        customers.removeIf(c -> c.getCpr().equals(cpr));
    }

    public String getCustomer(String cpr) {
        String notFound = "Customer Not Found";
        for (Customer c : customers) {
            if (c.getCpr().equals(cpr)) {
                System.out.println(cpr);
                return c.toString();
            }
        }

        return notFound;
    }


}
