Feature: Token Generation Feature

  Scenario: Token generation
    When a "TokenGenerationRequested" event is received
    Then a "TokenAssigned" event is sent
    And the token is assigned to the customer