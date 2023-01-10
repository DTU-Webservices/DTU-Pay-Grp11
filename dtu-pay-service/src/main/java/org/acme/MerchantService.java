package org.acme;

import java.util.*;

public class MerchantService {

    private final Set<Merchant> merchants = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    public MerchantService() {
        System.out.println("MerchantService Created");
    }

    public Merchant getMerchant() {
        return merchants.iterator().next();
    }

    public Set<Merchant> getMerchants() {
        return merchants;
    }

    public void addMerchant(Merchant m) {
        merchants.add(m);
    }

    public void removeMerchant(Merchant m) {
        merchants.remove(m);
    }

    public void removeMerchantByCpr(String cpr) {
        merchants.removeIf(m -> m.getCpr().equals(cpr));
    }

    public Merchant getMerchant(String mid) {
        for (Merchant m : merchants) {
            if (m.getMid().equals(mid)) {
                return m;
            }
        }
        return null;
    }
}
