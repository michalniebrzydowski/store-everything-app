package pl.edu.pb.storeeverything.dto.mapper;

import org.mapstruct.Mapper;
import pl.edu.pb.storeeverything.dto.CategoryDto;
import pl.edu.pb.storeeverything.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto map(Category category);

    Category map(CategoryDto categoryDto);

}
