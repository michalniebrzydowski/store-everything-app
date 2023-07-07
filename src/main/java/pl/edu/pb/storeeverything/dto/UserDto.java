package pl.edu.pb.storeeverything.dto;

import lombok.*;
import pl.edu.pb.storeeverything.entity.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class UserDto {

    private Long id;

    private String name;

    private String surname;

    private Integer age;

    private String login;

    private String email;

    private Boolean enabled;

    private Set<Role> roles;
}
