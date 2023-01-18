package org.acme.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author Kristoffer T. Pedersen, Lauritz Pepke, Oliver B. Klenum
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