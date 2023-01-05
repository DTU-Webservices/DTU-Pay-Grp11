package org.acme;

public class PaymentService {

    private Payment payment = new Payment("10","123","456");

    public PaymentService() {
        System.out.println("PaymentService Created");
    }

    public Payment getPayment() {
        return payment;
    }

    public void pay(Payment p) {
        payment = p;
    }

    public boolean validatePayment(Payment p) {
        return true;
    }
}
