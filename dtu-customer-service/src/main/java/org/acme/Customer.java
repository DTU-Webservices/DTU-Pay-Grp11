package org.acme;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID customerId = null;
    private String accountId;
    private UUID currentToken;
}