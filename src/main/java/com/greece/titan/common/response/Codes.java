package com.greece.titan.common.response;

public interface Codes {

    public enum Response {
        SUCCESS("00"),
        LOGIN("02"),
        ERROR("02");

        private String code;
        private String message;

        Response(final String code) {
            this.code = code;
        }

        Response(final String code, final String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            if (message == null) {
                return name().toLowerCase();
            } else {
                return message;
            }
        }

        public boolean accept(final String other) {
            return code.equals(other);
        }
    }

    public enum MsName {
        auth(8001),
        balance(8002),
        bill(8003),
        main(8004),
        membership(8005),
        modification(8006),
        product(8007),
        recharge(8008),
        gift(8014),
        offer(8015);

        private int port;

        private MsName(final int port) {
            this.port = port;
        }

        public int getPort() {
            return this.port;
        }
    }

}
