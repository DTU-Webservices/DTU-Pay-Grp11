package e2eTest.Entities;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Lauritz Pepke s191179
 */

@Data
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID customerId = null;
    private String accountId;
}