package com.dbs.escaperoom.models;

import javax.persistence.Entity;

/**
 * Created by Daan on 17-Oct-17.
 */
public class ExceptionMessage {
    private int HttpError;
    private String message;

    public ExceptionMessage(Exception exception){
        HttpError = 500;
        message = exception.getMessage();
    }

    public int getHttpError() {
        return HttpError;
    }

    public void setHttpError(int httpError) {
        HttpError = httpError;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
