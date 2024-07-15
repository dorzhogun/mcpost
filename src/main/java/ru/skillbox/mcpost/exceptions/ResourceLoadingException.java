package ru.skillbox.mcpost.exceptions;

public class ResourceLoadingException extends RuntimeException {
    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
