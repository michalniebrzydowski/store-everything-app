package pl.edu.pb.storeeverything.service.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.edu.pb.storeeverything.dto.PageRequestDto;

import java.time.*;

@Component
public class ModelBuilder {

    private static final ZoneId TIME_ZONE = ZoneId.of("Europe/Warsaw");
    private static final ZoneOffset ZONE_OFFSET = TIME_ZONE.getRules().getOffset(Instant.now());

    public OffsetDateTime buildEndDate(String endDate) {
        return LocalDate.parse(endDate)
                .atStartOfDay()
                .plusDays(1)
                .minusNanos(1)
                .atOffset(ZONE_OFFSET);
    }

    public OffsetDateTime buildStartDate(String startDate) {
        return LocalDate.parse(startDate)
                .atStartOfDay()
                .atOffset(ZONE_OFFSET);
    }

    public PageRequest createPageable(PageRequestDto pageRequestDto) {
        if ("categoryPopularity".equals(pageRequestDto.getSortField())) {
            return PageRequest.of(
                    pageRequestDto.getPageNumber(),
                    pageRequestDto.getPageSize()
            );
        }

        return PageRequest.of(
                pageRequestDto.getPageNumber(),
                pageRequestDto.getPageSize(),
                Sort.by(pageRequestDto.getSortDirection(), pageRequestDto.getSortField())
        );
    }
}
