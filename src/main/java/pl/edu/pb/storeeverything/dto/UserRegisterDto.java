package pl.edu.pb.storeeverything.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {

    @NotBlank(message = "Login is required")
    @Size(min = 3, max = 20, message = "Login should be between 3 and 20 characters long")
    @Pattern(regexp = "^[a-z]+$", message = "Login should contain only lowercase letters")
    private String login;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password should be at least 5 characters long")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 20, message = "Name should be between 3 and 20 characters long")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$", message = "Name should start with a capital letter and contain only letters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 3, max = 50, message = "Surname should be between 3 and 50 characters long")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$", message = "Name should start with a capital letter and contain only letters")
    private String surname;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age should be at least 18 years old")
    @Max(value = 200, message = "Age should not exceed 200 years old")
    private Integer age;
}
