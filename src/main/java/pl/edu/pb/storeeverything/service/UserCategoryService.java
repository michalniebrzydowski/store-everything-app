package pl.edu.pb.storeeverything.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pb.storeeverything.dto.CategoryDto;
import pl.edu.pb.storeeverything.dto.mapper.CategoryMapper;
import pl.edu.pb.storeeverything.entity.Category;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.entity.UserCategory;
import pl.edu.pb.storeeverything.repository.UserCategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;

    private final CategoryService categoryService;

    private final UserService userService;

    private final DataDeletionService dataDeletionService;

    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getUserCategories(String email, Sort.Direction direction) {
        Sort sort = Sort.by(direction, "category.name");

        return userCategoryRepository.findByUserEmail(email, sort).stream()
                .map(UserCategory::getCategory)
                .map(categoryMapper::map)
                .toList();
    }

    public boolean existsByEmailAndCategoryId(String email, Long categoryId) {
        return userCategoryRepository.existsByUserEmailAndCategoryId(email, categoryId);
    }

    @Transactional
    public void addCategory(String email, CategoryDto categoryDto) {
        User user = userService.findByEmail(email);

        Category category = categoryService.findByName(categoryDto.getName())
                .orElseGet(() -> categoryService.create(categoryDto));

        if (userCategoryRepository.existsByUserEmailAndCategoryName(email, category.getName())) {
            throw new IllegalStateException("A category with the name '" + category.getName() + "' is already assigned to the user.");
        }

        UserCategory userCategory = UserCategory.builder()
                .category(category)
                .user(user)
                .build();

        userCategoryRepository.save(userCategory);
    }

    @Transactional
    public void deleteUserCategoryAndAssociatedData(String email, Long categoryId) {
        dataDeletionService.deleteUserCategoryAndAssociatedData(email, categoryId);
    }
}
