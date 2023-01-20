package org.acme.Entity;

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
    private UUID tokenId;
    private UUID customerId;
    private String currentToken;
    private String qty;
    // List of tokens customer has
    private List<UUID> tokens = new ArrayList<>();

    public void addToken(UUID token) {
        tokens.add(token);
    }

    public void removeToken(UUID token) {
        tokens.remove(token);
    }
}
