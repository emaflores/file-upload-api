package com.emaflores.file.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestControllerAdvice
public class FileControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorObject handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return new ErrorObject(new Date().getTime(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorObject handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        return new ErrorObject(new Date().getTime(), HttpStatus.NOT_FOUND.value(), ex.getReason(), request.getRequestURI());
    }

    public static class ErrorObject {
        private Error error;

        public ErrorObject(long timestamp, int status, String message, String path) {
            this.error = new Error(timestamp, status, message, path);
        }

        public Error getError() {
            return error;
        }

        public void setError(Error error) {
            this.error = error;
        }
    }

    public static class Error {
        private long timestamp;
        private int status;
        private String message;
        private String path;

        public Error(long timestamp, int status, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.message = message;
            this.path = path;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
