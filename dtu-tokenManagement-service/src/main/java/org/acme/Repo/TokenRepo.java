package org.acme.Repo;

import lombok.Data;
import org.acme.Token;

import java.util.HashMap;
import java.util.Map;

@Data
public class TokenRepo {
    private static HashMap<String, Token> Tokens = new HashMap<>();

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


    public static Token getToken(String customerId) {
        return Tokens.get(customerId);
    }

    public static int getNumberOfTokens(String customerId) {
        return Tokens.get(customerId).getTokens().size();
    }

    public static String getCustomerIdFromToken(String token) {
        for(Map.Entry<String, Token> entry: Tokens.entrySet()) {
            if(entry.getValue().getTokens().contains(token)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
