package org.acme.Repo;

import lombok.Data;
import org.acme.Entity.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Oliver Brink Klenum s193625
 *
 */
@Data
public class TokenRepo {
    private static HashMap<UUID, Token> Tokens = new HashMap<>();

    public static void addToken(Token token) {
        if (Tokens.containsKey(token.getCustomerId())) {
            for (int i = 0; i < Integer.parseInt(token.getQty()); i++) {
                Tokens.get(token.getCustomerId()).addToken(token.getTokens().get(i));
            }
        } else {
            Tokens.put(token.getCustomerId(), token);
        }
    }


    public static Token getToken(UUID customerId) {
        return Tokens.get(customerId);
    }

    public static int getNumberOfTokens(UUID customerId) {

        int numberOfTokens = Tokens.get(customerId).getTokens().size();
        return numberOfTokens;
    }

    public static UUID getCidToken(UUID token) {
        for (UUID key : Tokens.keySet()) {
            if (Tokens.get(key).getTokens().contains(token)) {
                return key;
            }
        }
        return null;
    }

}
