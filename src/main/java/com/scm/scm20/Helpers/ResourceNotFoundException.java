package com.scm.scm20.Helpers;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }

    public ResourceNotFoundException(){
        super("Resouce Not Found");
    }
    
}
