package com.rgabay.neowritesogm.domain;

/**
 * Created by rossgabay on 5/11/17.
 */
public enum RoleType {
    DEVELOPER("dev"),
    ARCHITECT("arch"),
    MANAGER("mgr");

    private String value;

    public String getValue() {
        return value;
    }


    RoleType(String value) {
        this.value = value;
    }
}