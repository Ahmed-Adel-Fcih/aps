package com.qeema.aps.common.utils;

public enum ServiceCommand {
    Tokenization("TOKENIZATION"), PURCHASE("PURCHASE"), REFUND("REFUND");

    String name;

    ServiceCommand(String commandName) {
        this.name = commandName;
    }

    public String getName() {
        return name;
    }

}
