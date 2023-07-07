package pl.edu.pb.storeeverything.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.edu.pb.storeeverything.dto.InformationDto;
import pl.edu.pb.storeeverything.dto.InformationEmailShareViewDto;
import pl.edu.pb.storeeverything.entity.Information;
import pl.edu.pb.storeeverything.entity.InformationEmailShare;

@Mapper(componentModel = "spring")
public interface InformationMapper extends DateMapper {

    @Mapping(target="categoryId", source="category.id")
    @Mapping(target="category", source="category.name")
    @Mapping(target = "creationDate", source = "creationDate", qualifiedByName = "mapTimeFromOffset")
    @Mapping(target = "ownerEmail", source = "user.email")
    InformationDto map(Information information);

    @Mapping(target="category", source="information.category.name")
    @Mapping(target="content", source="information.content")
    @Mapping(target="title", source="information.title")
    @Mapping(target="link", source="information.link")
    @Mapping(target = "creationDate", source = "information.creationDate", qualifiedByName = "mapTimeFromOffset")
    @Mapping(target = "ownerEmail", source = "information.user.email")
    InformationEmailShareViewDto map(InformationEmailShare emailShare);
}
