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

  Scenario: Get Customer Id for Report
    Given there is a customer with empty merchant id
    When a "ReportAllCustomerPayReq" event for a report is received
    Then a "CustomerIdGetResponse" event is sent for a customer with customer id assigned
    And the customer has an id assigned

  Scenario: Delete Customer Account
    Given there is a customer registered with non-empty values
    When a "CustomerAccDeleteReq" event is received for a customer account
    Then a "CustomerAccDeleteResponse" event is sent to delete
    And the customer account is deleted