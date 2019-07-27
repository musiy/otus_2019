package com.musiy.loginservice;

public enum Role {

    USER();

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
