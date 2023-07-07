package pl.edu.pb.storeeverything.dto.mapper;

import org.mapstruct.Named;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public interface DateMapper {

    @Named("mapTimeFromOffset")
    default String mapTime(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
                .map(offset -> offset.atZoneSameInstant(ZoneId.of("Europe/Warsaw")))
                .map(ZonedDateTime::toLocalDateTime)
                .map(datetime -> datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .orElse(null);
    }

    @Named("mapTimeToOffset")
    default OffsetDateTime mapTime(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        return OffsetDateTime.of(localDateTime, ZoneOffset.of("Europe/Warsaw"))
                .withOffsetSameInstant(ZoneOffset.UTC);
    }
}
