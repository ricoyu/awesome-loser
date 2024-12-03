package com.loserico.pattern.lsp;

public class Ostrich extends Bird {
    @Override
    void fly() {
        throw new UnsupportedOperationException("Ostrich cannot fly");
    }
}