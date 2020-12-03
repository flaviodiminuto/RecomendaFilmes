package com.sexto.ia.exceptions;

public class SessaoExpiradaException extends Throwable {
    @Override
    public String getMessage() {
        return "Sesao expirada";
    }
}
