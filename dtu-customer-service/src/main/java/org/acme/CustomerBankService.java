package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.CustomerRepo;

import java.util.UUID;

/**
 *
 * @author Kristoffer T. Pedersen
 * @author Oliver Brink Klenum
 *
 */


public class CustomerBankService {

    private static final String CUSTOMER_ACC_REGISTER = "CustomerAccRegistered";
    private static final String CUSTOMER_GET_ACCOUNT = "CustomerAccGet";
    private static final String CUSTOMER_ACCOUNT_RESPONSE = "CustomerAccResponse";
    private static final String CUSTOMER_ID_RESPONSE = "CustomerIdResponse";
    private static final String CUSTOMER_DELETE_RESPONSE = "CustomerAccDeleteResponse";
    private static final String CUSTOMER_ID_GET_RESPONSE = "CustomerIdGetResponse";

    MessageQueue queue;

    public CustomerBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("CustomerAccRegisterReq", this::handleCustomerAccountRegister);
        this.queue.addHandler("CustomerAccGetReq", this::handleCustomerAccountGet);
        this.queue.addHandler("GetCustomerAccForTransferReq", this::handleCustomerAccountGetForTransfer);
        this.queue.addHandler("GetCustomerIdForTransferReq", this::handleCustomerIdGetForTransfer);
        this.queue.addHandler("ReportAllCustomerPayReq", this::handleReportAllCustomerPaymentsRequest);
        this.queue.addHandler("CustomerAccDeleteReq", this::handleCustomerAccountDelete);
    }

    public void handleCustomerAccountRegister(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        customer.setCustomerId(correlationId.getId());
        CustomerRepo.addCustomer(customer);
        Event event = new Event(CUSTOMER_ACC_REGISTER, new Object[] { customer, correlationId });
        queue.publish(event);
    }

    public void handleReportAllCustomerPaymentsRequest(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        System.out.println("BeforeSET!!!!!!!!!!!!!!!: " + customer);
        customer.setCustomerId(customer.getCustomerId());
        customer = CustomerRepo.getCustomer(customer.getCustomerId());
        System.out.println("AfterSET!!!!!!!!!!!!!!!: " + customer);
        Event event = new Event(CUSTOMER_ID_GET_RESPONSE, new Object[] { customer, correlationId });
        queue.publish(event);
    }

    public void handleCustomerAccountGet(Event ev) {
        handleCustomerRequestsForDifferentQueues(CUSTOMER_GET_ACCOUNT, ev);
    }

    public void handleCustomerAccountGetForTransfer(Event ev) {
        handleCustomerRequestsForDifferentQueues(CUSTOMER_ACCOUNT_RESPONSE, ev);
    }

    public void handleCustomerIdGetForTransfer(Event ev) {
        handleCustomerRequestsForDifferentQueues(CUSTOMER_ID_RESPONSE, ev);
    }

    public void handleCustomerRequestsForDifferentQueues(String responseHandler, Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var token = customer.getCurrentToken();
        var correlationId = ev.getArgument(1, CorrelationId.class);
        customer = CustomerRepo.getCustomer(customer.getCustomerId());
        customer.setCurrentToken(token);
        Event event = new Event(responseHandler, new Object[] { customer, correlationId });
        queue.publish(event);
    }

    public void handleCustomerAccountDelete(Event ev) {
        var customerId = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        var customer = CustomerRepo.getCustomer(UUID.fromString(customerId));
        CustomerRepo.deleteCustomer(UUID.fromString(customerId));
        Event event = new Event(CUSTOMER_DELETE_RESPONSE, new Object[] { customer, correlationId });
        queue.publish(event);
    }

}