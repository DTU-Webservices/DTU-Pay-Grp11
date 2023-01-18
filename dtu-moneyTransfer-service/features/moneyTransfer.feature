Feature: Transfer Money

  Scenario: A payment is created
    Given there is a payment with non-empty mid and cid
    And there is a money transfer with empty mAccountId and cAccountId
    When a 'PaymentCreateReq' event for a payment is received
    Then a "GetMerchantAccForTransferReq" is sent with same correlation id
    When a "MerchantAccResponse" event is received with a mAccountId
    Then a "GetCustomerAccForTransferReq" is sent with same correlation id
    When a "CustomerAccResponse" event is received with a cAccountId
    Then a "PaymentCreated" event is sent


  Scenario: a report over all transfers is created
