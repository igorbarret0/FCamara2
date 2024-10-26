package request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(

        String name,

        @NotBlank(message = "Password is invalid")
        @Size(min = 8, message = "Password should contain at least 8 characters")
        String password,

        String cpf,

        LocalDate birthDate,

        String address

) {
}
