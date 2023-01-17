package org.acme.Entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@Data
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID tokenId;
    private UUID customerId;

    private String qty;
    // List of tokens customer has
    private List<UUID> tokens;

    public void addToken(UUID token) {
        tokens.add(token);
    }

    public void removeToken(UUID token) {
        tokens.remove(token);
    }
}
