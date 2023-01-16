package org.acme;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID merchantId;
    private String accountId;
}
