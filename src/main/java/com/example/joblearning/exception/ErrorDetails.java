package com.example.joblearning.exception;

import java.util.Date;

/**
 * Error details class for structured error responses.
 * This class demonstrates:
 * - Creating a structured error response for REST APIs
 */
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
