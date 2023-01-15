package org.acme.MoneyTransfer;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID paymentId = null;
    private String mid;
    private String cid;
    private String amount;
}