package org.acme.Repo;

import org.acme.Entity.MoneyTransfer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class MoneyTransferRepo {
    private static final HashMap<UUID, MoneyTransfer> moneyTransfers = new HashMap<>();

    public static void addMoneyTransfer(MoneyTransfer moneyTransfer) {
        moneyTransfers.put(moneyTransfer.getMtId(), moneyTransfer);
    }

    public static MoneyTransfer getMoneyTransfer(UUID mtId) {
        return moneyTransfers.get(mtId);
    }

    public static void updateMoneyTransfer(MoneyTransfer moneyTransfer) {
        moneyTransfers.put(moneyTransfer.getMtId(), moneyTransfer);
    }

    public static Set<MoneyTransfer> getAllPayments() {
        try {
            return new HashSet<>(moneyTransfers.values());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<MoneyTransfer> getAllPaymentsWhereCustomerIsInvolved(String cAccountId) {
        return moneyTransfers
                .values()
                .stream()
                .filter(moneyTransfer -> moneyTransfer
                        .getCAccountId()
                        .equals(cAccountId))
                .collect(Collectors.toSet());
    }

    public static String calculateTotalAmountOfMoney() {
        return moneyTransfers
                .values()
                .stream()
                .map(MoneyTransfer::getAmount)
                .reduce(String.valueOf(0), (a, b) -> a + b);
    }

}
