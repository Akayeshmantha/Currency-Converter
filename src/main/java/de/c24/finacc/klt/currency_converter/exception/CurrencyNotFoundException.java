package de.c24.finacc.klt.currency_converter.exception;

public class CurrencyNotFoundException extends  RuntimeException{
    public CurrencyNotFoundException(String message) {
        super(message);
    }
}
