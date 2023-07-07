package pl.edu.pb.storeeverything.service.utility;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.pb.storeeverything.dto.PageRequestDto;

import java.time.OffsetDateTime;
import java.util.Objects;

@Component
@AllArgsConstructor
public class DateValidator {

    private final ModelBuilder modelBuilder;

    public boolean hasValidDates(PageRequestDto request) {
        return hasStartDate(request) || hasEndDate(request);
    }

    public boolean hasBothDatesValid(PageRequestDto request) {
        return hasStartDate(request) && hasEndDate(request);
    }

    public boolean hasEndDate(PageRequestDto request) {
        return Objects.nonNull(request.getEndDate())
                && !request.getEndDate().isBlank();
    }

    public boolean hasStartDate(PageRequestDto request) {
        return Objects.nonNull(request.getStartDate())
                && !request.getStartDate().isBlank();
    }

    public boolean isStartDateBeforeEndDate(PageRequestDto request) {
        if(!hasBothDatesValid(request)) {
            return true;
        }

        OffsetDateTime start = modelBuilder.buildStartDate(request.getStartDate());
        OffsetDateTime end = modelBuilder.buildEndDate(request.getEndDate());

        return start.isBefore(end);
    }

}
