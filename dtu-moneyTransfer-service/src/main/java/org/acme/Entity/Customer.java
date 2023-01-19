package org.acme.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Kristoffer Torngaard Pedersen s205354
 * @author Lauritz Pepke s191179
 */
@Data
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID customerId = null;
    private UUID currentToken = null;
    private String accountId;

}
