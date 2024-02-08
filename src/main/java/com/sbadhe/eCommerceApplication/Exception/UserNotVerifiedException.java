package com.sbadhe.eCommerceApplication.Exception;

public class UserNotVerifiedException extends Exception{
    private Boolean newEmailSent;

    public UserNotVerifiedException(Boolean newEmailSent) {
        this.newEmailSent = newEmailSent;
    }

    public boolean isNewEmailSent(){
        return newEmailSent;
    }
}
