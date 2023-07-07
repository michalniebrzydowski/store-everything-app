package pl.edu.pb.storeeverything.dto.mapper;

import org.mapstruct.Mapper;
import pl.edu.pb.storeeverything.dto.UserDto;
import pl.edu.pb.storeeverything.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map(User user);

    User map(UserDto userDto);
}
