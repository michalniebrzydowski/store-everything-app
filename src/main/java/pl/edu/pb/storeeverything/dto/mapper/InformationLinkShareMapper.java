package pl.edu.pb.storeeverything.dto.mapper;

import org.mapstruct.Mapper;
import pl.edu.pb.storeeverything.dto.InformationLinkShareDto;
import pl.edu.pb.storeeverything.entity.InformationLinkShare;
@Mapper(componentModel = "spring")
public interface InformationLinkShareMapper {

    InformationLinkShareDto map(InformationLinkShare linkShare);

    InformationLinkShare map(InformationLinkShareDto linkShareDto);
}
