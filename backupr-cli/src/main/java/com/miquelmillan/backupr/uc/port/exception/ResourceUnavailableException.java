package com.miquelmillan.backupr.uc.port.exception;

public class ResourceUnavailableException extends Exception {
    public ResourceUnavailableException(){
        super();
    }

    public ResourceUnavailableException(String message){
        super(message);
    }
}
