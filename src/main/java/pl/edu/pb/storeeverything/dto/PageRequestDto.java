package pl.edu.pb.storeeverything.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@Builder
@With
public class PageRequestDto {
    private Sort.Direction sortDirection;

    private String sortField;

    private Integer pageNumber;

    @Min(value = 10, message = "Page size must be at least 10")
    @Max(value = 50, message = "Page size must not exceed 50")
    private Integer pageSize;

    private String startDate;

    private String endDate;

    private Long categoryId;

    public PageRequestDto() {
        this.pageNumber = 0;
        this.pageSize = 10;
    }
}
