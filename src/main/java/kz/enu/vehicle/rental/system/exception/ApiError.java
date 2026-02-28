package kz.enu.vehicle.rental.system.exception;

import java.time.LocalDateTime;

public class ApiError {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String path;

    public ApiError(int status, String error, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
