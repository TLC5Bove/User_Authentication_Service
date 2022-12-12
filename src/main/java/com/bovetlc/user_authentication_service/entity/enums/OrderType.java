package com.bovetlc.user_authentication_service.entity.enums;

public enum OrderType {
    LIMIT("LIMIT"),
    MARKET("MARKET");

    private final String type;

    private OrderType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
