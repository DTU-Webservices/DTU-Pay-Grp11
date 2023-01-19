package e2eTest;

import e2eTest.Entities.*;

import javax.ws.rs.client.Entity;
import java.util.UUID;

public class TestService {

    public Merchant registerMerchant(Merchant merchant) {
        var response = WebTargetFactory.getWebTarget().path("merchants")
                .request()
                .post(Entity.json(merchant), Merchant.class);
        WebTargetFactory.close();
        return response;
    }

    public Merchant getMerchant(UUID merchantId) {
        var response = WebTargetFactory.getWebTarget().path("merchants")
                .path(merchantId.toString())
                .request()
                .get(Merchant.class);
        WebTargetFactory.close();
        return response;
    }

    public Customer registerCustomer(Customer customer) {
        var response = WebTargetFactory.getWebTarget().path("customers")
                .request()
                .post(Entity.json(customer), Customer.class);
        WebTargetFactory.close();
        return response;
    }

    public Customer getCustomer(UUID customerId) {
        var response = WebTargetFactory.getWebTarget().path("customers")
                .path(customerId.toString())
                .request()
                .get(Customer.class);
        WebTargetFactory.close();
        return response;
    }

    public String generateTokens(Token token) {
        var response = WebTargetFactory.getWebTarget().path("customers/tokens")
                .request()
                .post(Entity.json(token), String.class);
        WebTargetFactory.close();
        return response;
    }

    public String getToken(UUID customerId) {
        var response = WebTargetFactory.getWebTarget().path("customers/tokens")
                .path(customerId.toString())
                .request()
                .get(String.class);
        WebTargetFactory.close();
        return response;
    }

    public MoneyTransfer makePaymentAndTransferMoney(Payment payment) {
        var response = WebTargetFactory.getWebTarget().path("merchants/payment")
                .request()
                .post(Entity.json(payment), MoneyTransfer.class);
        WebTargetFactory.close();
        return response;
    }

    public Report getAllPaymentsForManager() {
        var response = WebTargetFactory.getWebTarget().path("reports")
                .request()
                .get(Report.class);
        WebTargetFactory.close();
        return response;
    }

    public Report getAllPaymentsMadeByCustomer(UUID customerId) {
        var response = WebTargetFactory.getWebTarget().path("customers/reports")
                .path(customerId.toString())
                .request()
                .get(Report.class);
        WebTargetFactory.close();
        return response;
    }

    public  Report getAllPaymentsMadeByMerchant(UUID merchantId) {
        var response = WebTargetFactory.getWebTarget().path("merchants/reports")
                .path(merchantId.toString())
                .request()
                .get(Report.class);
        WebTargetFactory.close();
        return response;
    }
}

