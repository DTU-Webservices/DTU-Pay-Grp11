package org.acme.Repo;

import org.acme.MoneyTransfer;

import java.util.HashMap;
import java.util.UUID;

public class MoneyTransferRepo {
    private static final HashMap<UUID, MoneyTransfer> moneyTransfers = new HashMap<>();

    public static void addMoneyTransfer(MoneyTransfer moneyTransfer) {
        moneyTransfers.put(moneyTransfer.getMtId(), moneyTransfer);
    }

    public static MoneyTransfer getMoneyTransfer(UUID mtId) {
        return moneyTransfers.get(mtId);
    }

    public static MoneyTransfer updateMoneyTransfer(MoneyTransfer moneyTransfer) {
        return moneyTransfers.put(moneyTransfer.getMtId(), moneyTransfer);
    }

}
