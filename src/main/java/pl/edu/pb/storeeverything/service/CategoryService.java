package pl.edu.pb.storeeverything.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pl.edu.pb.storeeverything.dto.CategoryDto;
import pl.edu.pb.storeeverything.entity.Category;
import pl.edu.pb.storeeverything.repository.CategoryRepository;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryService {

    private static final String NOUN_API = "https://noun-api-production.up.railway.app/api/is-noun/%s";

    private final CategoryRepository categoryRepository;

    private final RestTemplate restTemplate;

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID: [%s] not found".formatted(id)));
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public Category create(CategoryDto categoryDTO) {
        String word = categoryDTO.getName();

        String apiUrl = NOUN_API.formatted(word);

        try {
            boolean isNoun = Boolean.TRUE.equals(restTemplate.getForObject(apiUrl, Boolean.class));
            if (!isNoun) {
                throw new IllegalArgumentException("The provided word is not a noun: %s".formatted(word));
            }
        } catch (HttpServerErrorException e) {
            log.error("Failed to connect to the API: [%s]. Noun check was skipped".formatted(apiUrl));
        }

        Category category = Category.builder()
                .name(word)
                .build();

        return categoryRepository.saveAndFlush(category);
    }
}
