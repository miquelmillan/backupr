package com.miquelmillan.context.domain.resource;

public class ResourceUnavailableException extends Exception {
    public ResourceUnavailableException(){
        super();
    }

    public ResourceUnavailableException(String message){
        super(message);
    }
}
