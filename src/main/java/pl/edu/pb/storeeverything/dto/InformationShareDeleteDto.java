package pl.edu.pb.storeeverything.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class InformationShareDeleteDto {
    Long informationId;

    String email;
}