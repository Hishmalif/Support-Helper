package org.hishmalif.supporthelperbot.data.exception;

public class BotException extends RuntimeException {
    public BotException(Exception e) {
        super(e);
    }
}