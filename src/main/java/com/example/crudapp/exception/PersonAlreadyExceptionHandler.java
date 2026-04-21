package com.example.crudapp.exception;

public class PersonAlreadyExceptionHandler extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    public PersonAlreadyExceptionHandler(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() { return messageKey; }
    public Object[] getArgs() { return args; }
}
