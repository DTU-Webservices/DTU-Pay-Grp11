package org.acme.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Kristoffer T. Pedersen s205354.
 * @author Oliver Brink Klenum s193625
 * @author Lauritz Pepke s191179
 *
 */
@Data
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID paymentId = null;
    private String token;
    private String mid;
    //private String cid;
    private String amount;
}