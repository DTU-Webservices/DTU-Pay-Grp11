package org.acme;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor

public class Merchant {
    private String name;
    private String cpr;
    private String address;
    private String mid;
}
