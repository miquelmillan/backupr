package com.miquelmillan.backupr.domain.resource.exception;

public class ResourceRepositoryException extends Exception {
    public ResourceRepositoryException(){
        super();
    }

    public ResourceRepositoryException(String message){
        super(message);
    }
}
