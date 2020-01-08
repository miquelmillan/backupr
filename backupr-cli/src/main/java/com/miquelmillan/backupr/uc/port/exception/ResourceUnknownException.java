package com.miquelmillan.backupr.uc.port.exception;

public class ResourceUnknownException extends Exception {
    public ResourceUnknownException(){
        super();
    }

    public ResourceUnknownException(String message){
        super(message);
    }
}
