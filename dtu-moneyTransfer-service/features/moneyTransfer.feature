Feature: Transfer Money

  Scenario: A payment is created
    Given there is a payment with non-empty mid and cid
    And there is a money transfer with empty mAccountId and cAccountId
    When a 'PaymentCreateReq' event for a payment is received
    Then a "GetMerchantAccForTransferReq" is sent with same correlation id
    When a "MerchantAccResponse" event is received with a mAccountId
    Then a "GetCustomerIdFromTokenReq" is sent with same correlation id after mAccountId assigned
    When a "GetCustomerIdFromTokenRes" event is received with a customer token
    Then a "GetCustomerAccForTransferReq" is sent with same correlation id after token assigned
    When a "CustomerAccResponse" event is received with a cAccountId
    Then a "PaymentCreated" event is with the money transfer and correlation id
    And the money transfer has an mAccountId and cAccountId assigned

  Scenario: A report is generated for all transfers
    Given there is a money transfer with non-empty mAccountId, cAccountId and amount
    When a "ReportAllPayReq" event is received for a report with correlation id
    Then a "ReportAllPay" event is sent with same correlation id
    And a report with all payments is generated

  Scenario: A report is generated for a customer
    Given there is a money transfer with non-empty mAccountId, cAccountId and amount
    When a "CustomerIdGetResponse" event is received for a customer
    Then a "ReportAllCustomerPay" event is sent for a customer with matching correlation id
    And a report with all payments is generated

  Scenario: A report is generated for a merchant
    Given there is a money transfer with non-empty mAccountId, cAccountId and amount
    When a "MerchantIdGetResponse" event is received for a merchant
    Then a "ReportAllMerchantPay" event is sent for a merchant with matching correlation id
    And a report with all payments is generated


