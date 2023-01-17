package org.acme.Repo;

import lombok.Data;
import org.acme.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class TokenRepo {
    private static HashMap<UUID, Token> Tokens = new HashMap<>();

    public static void addToken(Token token) {
        // if customerId is in repo only add token to list
        if (Tokens.containsKey(token.getCustomerId())) {
            for (int i = 0; i < Integer.parseInt(token.getQty()); i++) {
                Tokens.get(token.getCustomerId()).addToken(token.getTokens().get(i));
            } // else add new customer to repo
        } else {
            Tokens.put(token.getCustomerId(), token);
        }
    }


    public static Token getToken(UUID customerId) {
        return Tokens.get(customerId);
    }

    public static int getNumberOfTokens(UUID customerId) {
        return Tokens.get(customerId).getTokens().size();
    }

    public static UUID getCustomerIdFromToken(UUID token) {
        for(Map.Entry<UUID, Token> entry: Tokens.entrySet()) {
            if(entry.getValue().getTokens().contains(token)) {
                var customerId = entry.getKey();
                System.out.println(customerId);
                return customerId;
            }
        }
        return null;
    }
/*
    public static UUID getCidToken(UUID token) {
        for (UUID key : Tokens.keySet()) {
            for (int i = 0; i < Tokens.get(key).getTokens().size(); i++) {
                if (Tokens.get(key).getTokens().get(i).equals(token)) {
                    return key;
                }
            }

        }
        return null;
    }

 */

    public static UUID getCidToken(UUID token) {
        for (UUID key : Tokens.keySet()) {
            if (Tokens.get(key).getTokens().contains(token)) {
                return key;
            }
        }
        return null;
    }

}
