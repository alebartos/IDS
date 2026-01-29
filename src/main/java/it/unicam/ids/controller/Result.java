package it.unicam.ids.controller;

/**
 * Classe che rappresenta il risultato di un'operazione.
 * Sostituisce ResponseEntity per la versione Java pura.
 * Pronto per futura integrazione con Spring Boot.
 */
public class Result<T> {

    private final T data;
    private final String errorMessage;
    private final int statusCode;
    private final boolean success;

    private Result(T data, String errorMessage, int statusCode, boolean success) {
        this.data = data;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
        this.success = success;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, 200, true);
    }

    public static <T> Result<T> created(T data) {
        return new Result<>(data, null, 201, true);
    }

    public static <T> Result<T> error(String message, int statusCode) {
        return new Result<>(null, message, statusCode, false);
    }

    public static <T> Result<T> badRequest(String message) {
        return new Result<>(null, message, 400, false);
    }

    public static <T> Result<T> notFound(String message) {
        return new Result<>(null, message, 404, false);
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        if (success) {
            return "Result{success=true, statusCode=" + statusCode + ", data=" + data + "}";
        } else {
            return "Result{success=false, statusCode=" + statusCode + ", error='" + errorMessage + "'}";
        }
    }
}
