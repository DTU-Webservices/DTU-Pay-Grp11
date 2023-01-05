package org.acme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

public class PaymentService {

    private final Set<Payment> payments = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public PaymentService() {
        payments.add(new Payment("100", "1234567890", "1234567890"));
        payments.add(new Payment("100", "123456227890", "1234567890"));
        payments.add(new Payment("100", "123456227890", "1234567890"));
        System.out.println("PaymentService Created");
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

    public boolean validatePayment(Payment p) {
        return true;
    }
}
