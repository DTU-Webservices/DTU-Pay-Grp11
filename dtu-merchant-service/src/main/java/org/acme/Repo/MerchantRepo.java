package org.acme.Repo;

import lombok.Data;
import org.acme.Merchant;

import java.util.HashMap;
import java.util.UUID;

@Data
public class MerchantRepo {
    private static HashMap<UUID, Merchant> Merchants = new HashMap<>();

    public static void addMerchant(Merchant merchant) {
        Merchants.put(merchant.getMerchantId(), merchant);
    }

    public static Merchant getMerchant(UUID merchantId) {
        return Merchants.get(merchantId);
    }
}
