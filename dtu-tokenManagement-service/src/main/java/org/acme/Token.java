package org.acme;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@Data
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID tokenId;
    private String accountId;

    private Integer qty;
    // List of tokens customer has
    private List<String> tokens;

    public void addToken(String token) {
        tokens.add(token);
    }
}
