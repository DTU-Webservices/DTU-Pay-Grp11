package e2eTest.Entities;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class MoneyTransfer implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID mtId;
    private String mAccountId;
    private String cAccountId;
    private String amount;
    private String description = "No description"; // optional
}
