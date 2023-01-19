package org.acme.Repo;

import lombok.Data;
import org.acme.Entity.Payment;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Kristoffer T. Pedersen s205354.
 */
@Data
public class PaymentRepo {

    private static HashMap<UUID, Payment> Payments = new HashMap<>();

    public static void addPayment(Payment payment) {
        Payments.put(payment.getPaymentId(), payment);
    }

    public static Payment getPayment(UUID paymentId) {
        return Payments.get(paymentId);
    }

    public static Payment getFromMerchantId(String merchantId) {
        for (Payment payment : Payments.values()) {
            if (payment.getMid().equals(merchantId)) {
                return payment;
            }
        }
        return null;
    }
}
