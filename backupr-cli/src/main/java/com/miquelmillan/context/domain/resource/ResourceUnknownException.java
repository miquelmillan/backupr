package com.miquelmillan.context.domain.resource;

public class ResourceUnknownException extends Exception {
    public ResourceUnknownException(){
        super();
    }

    public ResourceUnknownException(String message){
        super(message);
    }
}
