package pl.edu.pb.storeeverything.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.edu.pb.storeeverything.dto.InformationEmailShareDto;
import pl.edu.pb.storeeverything.entity.InformationEmailShare;
@Mapper(componentModel = "spring")
public interface InformationEmailShareMapper {
    @Mapping(target = "userEmail", source = "user.email")
    InformationEmailShareDto map(InformationEmailShare emailShare);

    InformationEmailShare map(InformationEmailShareDto emailShare);
}
