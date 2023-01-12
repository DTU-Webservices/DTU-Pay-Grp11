package org.acme.Demo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String demoMessage;

}
