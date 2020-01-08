package com.miquelmillan.backupr.domain.resource.exception;

public class ResourceUnknownException extends Exception {
    public ResourceUnknownException(){
        super();
    }

    public ResourceUnknownException(String message){
        super(message);
    }
}
