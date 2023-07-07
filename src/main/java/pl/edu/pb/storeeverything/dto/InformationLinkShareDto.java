package pl.edu.pb.storeeverything.dto;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class InformationLinkShareDto {

    private Long informationId;

    private String shareLink;

    private Boolean editPermission;
}
