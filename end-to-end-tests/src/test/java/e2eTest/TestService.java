package e2eTest;

import e2eTest.Entities.*;

import javax.ws.rs.client.Entity;
import java.util.UUID;

public class TestService {

    public Merchant registerMerchant(Merchant merchant) {
        return WebTargetFactory.getWebTarget().path("merchants")
                .request()
                .post(Entity.json(merchant), Merchant.class);
    }

    public Merchant getMerchant(UUID merchantId) {
        return WebTargetFactory.getWebTarget().path("merchants")
                .path(merchantId.toString())
                .request()
                .get(Merchant.class);
    }

    public Customer registerCustomer(Customer customer) {
        return WebTargetFactory.getWebTarget().path("customers")
                .request()
                .post(Entity.json(customer), Customer.class);
    }

    public Customer getCustomer(UUID customerId) {
        return WebTargetFactory.getWebTarget().path("customers")
                .path(customerId.toString())
                .request()
                .get(Customer.class);
    }

    public String generateTokens(Token token) {
        return WebTargetFactory.getWebTarget().path("customers/tokens")
                .request()
                .post(Entity.json(token), String.class);
    }

    public String getToken(UUID customerId) {
        return WebTargetFactory.getWebTarget().path("customers/tokens")
                .path(customerId.toString())
                .request()
                .get(String.class);
    }

    public MoneyTransfer makePaymentAndTransferMoney(Payment payment) {
        return WebTargetFactory.getWebTarget().path("merchants/payment")
                .request()
                .post(Entity.json(payment), MoneyTransfer.class);
    }
}

