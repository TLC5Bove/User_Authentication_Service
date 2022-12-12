package com.bovetlc.user_authentication_service.entity.enums;

public enum Side {
    BUY("BUY"),
    SELL("SELL");

    private final String side;

    private Side(String side){
        this.side = side;
    }

    public String getSide() {
        return side;
    }
}
