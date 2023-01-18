package org.acme.Entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID reportId;
    private Set<MoneyTransfer> moneyTransfers;
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
