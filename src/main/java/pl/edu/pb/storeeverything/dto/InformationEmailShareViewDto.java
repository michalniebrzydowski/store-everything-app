package pl.edu.pb.storeeverything.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class InformationEmailShareViewDto {
    private Long id;

    @NotBlank(message = "Category name cannot be empty.")
    @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters.")
    private String title;

    @NotBlank(message = "Category name cannot be empty.")
    @Size(min = 5, max = 500, message = "Content must be between 5 and 500 characters.")
    private String content;

    private String link;

    private String category;

    private String ownerEmail;

    private String creationDate;

    private Boolean editPermission;
}
