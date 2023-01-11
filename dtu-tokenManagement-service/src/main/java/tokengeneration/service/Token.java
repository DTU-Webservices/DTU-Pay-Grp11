package tokengeneration.service;

import java.io.Serializable;

import lombok.Data;

public class Token implements Serializable {

        private static final long serialVersionUID = 9023222981284806610L;
        private String token;
        private String id;
        private String cid;
}