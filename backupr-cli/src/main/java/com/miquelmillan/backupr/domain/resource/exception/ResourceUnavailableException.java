package com.miquelmillan.backupr.domain.resource.exception;

public class ResourceUnavailableException extends Exception {
    public ResourceUnavailableException(){
        super();
    }

    public ResourceUnavailableException(String message){
        super(message);
    }
}
