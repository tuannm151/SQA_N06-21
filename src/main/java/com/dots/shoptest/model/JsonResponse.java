package com.dots.shoptest.model;

public class JsonResponse {
    private String status;
    private String message;
    private Object data;

    public JsonResponse() {
    }

    public JsonResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public JsonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static JsonResponse success(String message) {
        return new JsonResponse("success", message);
    }

    public static JsonResponse error(String message) {
        return new JsonResponse("error", message);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
