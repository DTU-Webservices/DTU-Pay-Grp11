Feature: DTU Customer Service feature

  Scenario: Customer Bank Account Assigned
    When a "CustomerBankAccRequested" event is received
    Then a "TokenValidationRequested" event is sent
    When a "TokenValidated" event is received
    Then the "CustomerBankAccAssigned" event is sent with non-empty id
    And the payment gets a merchant bank account id