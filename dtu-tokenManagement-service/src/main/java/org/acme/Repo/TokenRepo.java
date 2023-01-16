package org.acme.Repo;

import lombok.Data;
import org.acme.Token;

import java.util.HashMap;

@Data
public class TokenRepo {
    private static HashMap<String, Token> Tokens = new HashMap<>();

    public static void addToken(Token token) {
        Tokens.put(token.getCustomerId(), token);
    }


    public static Token getToken(String accountId) {
        return Tokens.get(accountId);
    }

    public static Token getTokens() {
        for (Token token : Tokens.values()) {
            return token;
        }
        return null;
    }

}
