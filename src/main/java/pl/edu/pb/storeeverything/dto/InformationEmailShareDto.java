package pl.edu.pb.storeeverything.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class InformationEmailShareDto {

    private Long informationId;

    private Boolean editPermission;

    private String userEmail;
}



