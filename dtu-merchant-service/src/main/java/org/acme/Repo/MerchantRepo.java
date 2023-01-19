package org.acme.Repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.Merchant;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Kristoffer Torngaard Pedersen s205354
 */
@Data
public class MerchantRepo {
    private static HashMap<UUID, Merchant> Merchants = new HashMap<>();

    public static void addMerchant(Merchant merchant) {
        Merchants.put(merchant.getMerchantId(), merchant);
    }

    public static Merchant getMerchant(UUID merchantId) {
        return Merchants.get(merchantId);
    }

    public static void deleteMerchant(UUID merchantId) {
        Merchants.remove(merchantId);
    }
}