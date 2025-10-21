package springboot_security_config.springboot_security.com.secure;

import lombok.Data;

/**
 * Created by prashant.mod on 22-07-2024 Monday 2:20:01 pm
 *
 */
@Data
public class ErrorResponse {
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
