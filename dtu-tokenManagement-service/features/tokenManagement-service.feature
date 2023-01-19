Feature: Token Service Feature

  Scenario: Token Generation
    When a "TokensGenerateReq" event for a token is received
    Then a "TokensGenerated" event is sent with the same correlation id
    And the token is generated

  Scenario: Get token for CustomerId
    Given there is a customer registered with available tokens
    When a "TokensGetTokenReq" event for a customer id is received
    Then a "TokensGetToken" event is sent with the same correlation id and the token
    And the token is returned

  Scenario: Get amount of tokens for CustomerId
    Given there is a customer registered with tokens
    When a "TokensAmountGetReq" event for a customer is received
    Then a "TokensAmountGet" event is sent with the same correlation id and the amount of tokens
    And the amount of tokens is returned

  Scenario: Get customerId from token
    Given there is a customer registered with a token
    Given there is a payment registered with a token
    When a "GetCustomerIdFromTokenReq" event for a customer token is received
    Then a "GetCustomerIdFromTokenRes" event is sent with the same correlation id and the customer id
    And the customer id is returned