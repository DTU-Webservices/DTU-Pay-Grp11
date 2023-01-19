package org.acme.Entities;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Lauritz Pepke s191179.
 *
 */

@Data
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID reportId;
    private Set<MoneyTransfer> moneyTransfers;
    private BigDecimal totalAmount = null;
}
