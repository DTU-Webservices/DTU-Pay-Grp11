Feature: Customer token generation feature

  Scenario: Customer token generation
    Given that a customer is registered with DTUPay
    And the customer has one or less tokens
    When the customer requests a token
    Then "CustomerTokensRequested" event is published
    When the CustomerTokensAssigned event is received with customer token amount not exceeding 6 tokens
    Then the customer tokens are registered
