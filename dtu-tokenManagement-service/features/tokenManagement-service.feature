Feature: Token Generation Feature

  Scenario: Token Generation
    When a "TokensGenerateReq" event for a token is received
    Then a "TokensGenerated" event is sent with the same correlation id
    And the token is generated