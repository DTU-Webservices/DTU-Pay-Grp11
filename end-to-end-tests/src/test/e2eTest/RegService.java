package e2eTest;

import javax.ws.rs.client.Entity;
import java.util.UUID;

public class RegService {

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
}

