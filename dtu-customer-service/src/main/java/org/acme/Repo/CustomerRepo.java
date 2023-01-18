package org.acme.Repo;

import lombok.Data;
import org.acme.Customer;


import java.util.HashMap;
import java.util.UUID;

@Data
public class CustomerRepo {
    private static HashMap<UUID, Customer> Customers = new HashMap<>();

    public static void addCustomer(Customer customer) {
        Customers.put(customer.getCustomerId(), customer);
    }

    public static Customer getCustomer(UUID customerId) {
        return Customers.get(customerId);
    }

    public static void deleteCustomer(UUID customerId) {
        Customers.remove(customerId);
    }
}

