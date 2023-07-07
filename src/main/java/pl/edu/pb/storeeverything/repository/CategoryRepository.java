package pl.edu.pb.storeeverything.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pb.storeeverything.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
