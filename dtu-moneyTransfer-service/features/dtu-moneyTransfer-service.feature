Feature: DTU MoneyTransfer Service

  Scenario: Money is successfully Transferred from Customer to Merchant
    Given there is a payment with empty amount, token and mid
    When the payment is being initiated
    Then a "AmountRequested" event is sent
    And  a "MerchantBankAccRequested" event is sent
    And a "CustomerBankAccRequested" event is sent
    When a "AmountAssigned" event for the payment is sent with non-empty amount
    And a "MerchantBankAccAssigned" event for the payment is sent with non-empty mid
    And a "CustomerBankAccAssigned" event for the payment is sent with non-empty token
    Then the payment is set with an amount, a token and a mid
    When the "MoneyTransferred" event for a payment is sent
    Then the amount is deducted from the Customer bank account
    And the amount is added to the Merchant bank account