package com.ht.dev.mspatient.util;

/**
 * Mai 2017
 * @author maj
 */
public class CustomErrorType {
    
    //message d'erreur de retour Json error
    private final String error;
 
    public CustomErrorType(String errorMessage){
        this.error = errorMessage;
    }
 
    public String getErrorMessage() {
        return error;
    }
 
}
