package com.bovetlc.user_authentication_service.entity.enums;

public enum Status {
    PENDING("pending"),
//    PARTIAL("partial"),
    CANCELLED("cancelled"),
    COMPLETE("complete");

    private final String status;

    private Status(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
