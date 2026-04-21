package com.example.crudapp.exception;

public class ItemAlreadyExistsException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    public ItemAlreadyExistsException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }

    public ItemAlreadyExistsException(String messageKey, Throwable cause, Object... args) {
        super(messageKey, cause);
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() { return messageKey; }
    public Object[] getArgs() { return args; }
}