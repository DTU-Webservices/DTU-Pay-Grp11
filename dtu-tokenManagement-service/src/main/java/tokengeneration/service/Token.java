package tokengeneration.service;

import java.io.Serializable;

import lombok.Data;

public class Token implements Serializable {

        private static final long serialVersionUID = 9023222981284806610L;
        private String token;
        private String id;
        private String cid;

        public String getToken() {
                return token;
        }

        public void setToken(String token) {
                this.token = token;
        }

        public String getCid() {
                return cid;
        }

        public void setCid(String cid) {
                this.cid = cid;
        }

        public String getId() {
                return id;
        }

        public void setId(int i) {
                this.id = id;
        }
}
