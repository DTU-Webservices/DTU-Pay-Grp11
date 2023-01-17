Feature: DTU Customer Service feature

  Scenario: Customer Account Registration
    When a "CustomerAccRegisterReq" event for a customer is received
    Then the "CustomerAccRegistered" event is sent with the same correlation id
    And the customer account is registered

  Scenario: Get Customer Account for Transfer
    Given there is a customer registered
    When a "CustomerAccForTransferReq" event for a transfer is received
    Then a "CustomerAccResponse" event for a transfer is sent with the same correlation id
    And the customer for a transfer is retrieved