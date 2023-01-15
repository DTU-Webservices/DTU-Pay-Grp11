package org.acme.Repo;

import lombok.Data;
import org.acme.Payment;
import java.util.HashMap;
import java.util.UUID;

@Data
public class PaymentRepo {

    private static HashMap<UUID, Payment> Payments = new HashMap<>();

    public static void addPayment(Payment payment) {
        Payments.put(payment.getPaymentId(), payment);
    }

    public static Payment getPayment(UUID paymentId) {
        return Payments.get(paymentId);
    }
}
