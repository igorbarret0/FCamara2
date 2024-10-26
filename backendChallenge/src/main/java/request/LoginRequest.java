package request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password should contain at least 8 characters")
        String password

) {
}
