//package com.g10.api.base.error;
//
//import org.springframework.http.HttpStatus;
//
//import java.util.List;
//
//public class Error {
//    private String message;
//    private HttpStatus status;
//    private int statusCode;
//    private String errorMessage;
//    private int errorCode;
//    private String description;
//    private List<FieldError> fields;
//
//    public Error(String message,
//                 HttpStatus status, Codes code) {
//        this.message = message;
//        this.status = status;
//        this.statusCode = status.value();
//        this.errorMessage = code.getError();
//        this.errorCode = code.getCode();
//        this.description = code.getDescription();
//    }
//
//    public Error(String message, HttpStatus status, Codes code, List<FieldError> fields) {
//        this.message = message;
//        this.status = status;
//        this.statusCode = status.value();
//        this.errorMessage = code.getError();
//        this.errorCode = code.getCode();
//        this.description = code.getDescription();
//        this.fields = fields;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public HttpStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(HttpStatus status) {
//        this.status = status;
//    }
//
//    public int getStatusCode() {
//        return statusCode;
//    }
//
//    public void setStatusCode(int statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public String getErrorMessage() {
//        return errorMessage;
//    }
//
//    public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }
//
//    public int getErrorCode() {
//        return errorCode;
//    }
//
//    public void setErrorCode(int errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public List<FieldError> getFields() {
//        return fields;
//    }
//
//    public void setFields(List<FieldError> fields) {
//        this.fields = fields;
//    }
//}
//
