//Cucumber Testing Payment

package org.acme;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.quarkus.test.junit.QuarkusTest;

Feature: Payment
Scenario: Successful payment
    Given a customer with id "cid1"
    And a merchant with id "mid1"
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful



