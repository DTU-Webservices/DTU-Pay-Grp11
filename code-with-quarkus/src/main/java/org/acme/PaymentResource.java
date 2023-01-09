package org.acme;

import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Objects;


@Path("/payment")
public class PaymentResource {

    private final PaymentService service = new PaymentService();
    private final AccountService accountService = new AccountService();

    @POST
    @Path("/createAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(String userJson) {
    	try {
            JSONObject obj = new JSONObject(userJson);

            String balance = obj.getString("balance");
            System.out.println("Balance: " + balance);

            JSONObject userObj = obj.getJSONObject("user");
            System.out.println(userObj);

            String cprNumber = userObj.getString("cprNumber");
            String firstName = userObj.getString("firstName");
            String lastName = userObj.getString("lastName");

            User user = new User();
            user.setCprNumber(cprNumber);
            user.setFirstName(firstName);
            user.setLastName(lastName);
    		String acc = service.createBankAccount(user, BigDecimal.valueOf(Integer.parseInt(balance)));
            Account account = new Account();
            account.setFirstname(firstName);
            account.setLastname(lastName);
            account.setCpr(cprNumber);
            account.setBankAddress(acc);
            account.setCid(acc);
            accountService.addCustomer(cprNumber, account);
    		return Response.ok()
                    .entity(acc)
                    .build();
    	} catch (BankServiceException_Exception e) {
            e.printStackTrace();
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    }

    @POST
    @Path("/getAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCustomerByCpr(String cpr) {
        try {
            JSONObject obj = new JSONObject(cpr);
            String cprNumber = obj.getString("cprNumber");
            String cus = accountService.getCustomer(cprNumber);
            return Response.ok()
                    .entity(cus)
                    .build();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @GET
    @Path("/getAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomers() {
        String cus = accountService.getCustomers().toString();
        return Response.ok()
                .entity(cus)
                .build();
    }


    @POST
    @Path("/transferMoney")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferMoney(String transferJson) {
    	try {
            JSONObject obj = new JSONObject(transferJson);

            String amount = obj.getString("amount");
            String from = obj.getString("fromAccountId");
            String to = obj.getString("toAccountId");
            if (accountService.getCustomer(from) != null && accountService.getCustomer(to) != null) {
                service.transferMoney(from, to, BigDecimal.valueOf(Integer.parseInt(amount)));
                return Response.ok()
                        .entity("Transfer successful")
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Account Not Found").build();
    	} catch (BankServiceException_Exception e) {
            e.printStackTrace();
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    }

    @GET
    @Path("/retireAllRegisteredAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retireAllAccounts() {
        try {
            accountService.retireRegisteredAccounts();
            return Response.ok()
                    .build();
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/retireAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response retireAccount(String accountId) {
    	try {
            JSONObject obj = new JSONObject(accountId);
            String acc = obj.getString("accountId");
    		service.retireAccount(acc);
    		return Response.ok()
                    .build();
    	} catch (BankServiceException_Exception e) {
            e.printStackTrace();
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
    	}
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Payment getPaymentJson() {
        return service.getPayment();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Payment getPaymentXml() {
        return service.getPayment();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentsJson() {
        return Response.ok(service.getPayments()).build();
    }

    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountsJson() throws BankServiceException_Exception {
        return Response.ok(service.getAccounts()).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pay(Payment p) {
        String cid = service.getCid(p);
        String mid = service.getMid(p);
        String messageCid = "customer with id " + cid + " is unknown";
        String messageMid = "merchant with id " + mid + " is unknown";
        if (Objects.equals(mid, "mid1") && !Objects.equals(cid, "cid1")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messageCid)
                    .build();
        } else if (Objects.equals(cid, "cid1") && !Objects.equals(mid, "mid1")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messageMid)
                    .build();
        } else {
            service.pay(p);
            return Response.ok()
                    .entity("payment successful")
                    .build();
        }
    }
}

/*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response payJson(Payment p) throws URISyntaxException {
        if (service.validatePayment(p)) {
            service.pay(p);
            System.out.println("Payment successful");
            return Response.created(new URI("/payment")).build();
        } else {
            System.out.println("Payment failed");
            return Response.status(Response.Status.BAD_REQUEST).build();

        }
    }

 */
