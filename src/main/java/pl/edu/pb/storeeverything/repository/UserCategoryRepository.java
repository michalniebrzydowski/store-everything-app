package pl.edu.pb.storeeverything.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pb.storeeverything.entity.UserCategory;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    void deleteByUserEmailAndCategoryId(String email, Long categoryId);

    List<UserCategory> findByUserEmail(String email, Sort sort);

    Long countByCategoryId(Long categoryId);

    boolean existsByUserEmailAndCategoryName(String email, String category);

    boolean existsByUserEmailAndCategoryId(String email, Long category);
}
