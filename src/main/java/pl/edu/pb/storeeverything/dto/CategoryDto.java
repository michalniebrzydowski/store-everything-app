package pl.edu.pb.storeeverything.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Category name cannot be empty.")
    @Size(min = 3, max = 20, message = "Category name must be between 3 and 20 characters.")
    @Pattern(regexp = "^[a-z]+$", message = "Category name can only contain lowercase letters.")
    private String name;
}
