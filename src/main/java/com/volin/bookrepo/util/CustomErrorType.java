package com.volin.bookrepo.util;

/**
 * The Class CustomErrorType.
 */
public class CustomErrorType {

    private String errorMessage;

    /**
     * Instantiates a new custom error type.
     *
     * @param errorMessage the error message
     */
    public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
