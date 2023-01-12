Feature: Token Generation Feature

  Scenario: Token generation
    Given there is a token with empty id
    When the token is being registered
    Then the "TokenGenerationRequested" event is published
    When the TokenIdAssigned event is received with non-empty id
    Then the token is registered and the id is set
