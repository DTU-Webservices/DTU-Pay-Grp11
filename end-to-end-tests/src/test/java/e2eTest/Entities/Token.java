package e2eTest.Entities;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Oliver Brink Klenum s193625
 *
 */

@Data
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID tokenId = null;
    private UUID customerId;

    private String qty;

    private List<String> tokens = new ArrayList<>();
}
