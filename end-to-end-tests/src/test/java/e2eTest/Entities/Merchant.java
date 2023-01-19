package e2eTest.Entities;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Kristoffer T. Pedersen s205354
 */

@Data
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID merchantId = null;
    private String accountId;
}
