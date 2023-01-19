package org.acme.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Oliver Brink Klenum s193625
 * @author Kristoffer T. Pedersen s205354.
 *
 */
@Data
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID customerId = null;
    private String accountId;
    private UUID currentToken;
}
