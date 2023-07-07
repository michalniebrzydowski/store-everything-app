package pl.edu.pb.storeeverything.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.pb.storeeverything.entity.Information;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {
    Page<Information> findByUserEmail(String email, Pageable pageable);

    Page<Information> findByUserEmailAndCategoryId(String email,Long categoryId, Pageable pageable);

    @Query("SELECT info.id FROM Information info WHERE info.user.email = :email AND info.category.id = :categoryId")
    List<Long> findAllInformationIdByUserEmailAndCategoryId(String email, Long categoryId);

    Page<Information> findByUserEmailAndCreationDateBetween(String email, OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    Page<Information> findByUserEmailAndCategoryIdAndCreationDateBetween(String email, Long categoryId, OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    Page<Information> findByUserEmailAndCreationDateBefore(String email, OffsetDateTime end, Pageable pageable);

    Page<Information> findByUserEmailAndCategoryIdAndCreationDateBefore(String email, Long categoryId,  OffsetDateTime end, Pageable pageable);

    @Query("""
    SELECT i FROM Information i
    WHERE i.user.email = :email
    ORDER BY
    (SELECT COUNT(*) FROM Information i2 WHERE i2.user = i.user AND i2.category = i.category) DESC, i.creationDate DESC
    """)
    Page<Information> findByUserEmailSortedByCategoryPopularityDescending(String email, Pageable pageable);

    @Query("""
    SELECT i FROM Information i
    WHERE i.user.email = :email
    AND i.creationDate BETWEEN :start AND :end
    ORDER BY
    (SELECT COUNT(*) FROM Information i2 WHERE i2.user = i.user AND i2.category = i.category AND i2.creationDate BETWEEN :start AND :end) DESC, i.creationDate DESC
    """)
    Page<Information> findByUserEmailAndDateBetweenSortedByCategoryPopularityDescending(String email, Pageable pageable, OffsetDateTime start, OffsetDateTime end);

    @Query("""
    SELECT i FROM Information i
    WHERE i.user.email = :email
    AND i.creationDate <= :end
    ORDER BY
    (SELECT COUNT(*) FROM Information i2 WHERE i2.user = i.user AND i2.category = i.category AND i2.creationDate BETWEEN :start AND :end) DESC, i.creationDate DESC
    """)
    Page<Information> findByUserEmailAndDateBetweenSortedByCategoryPopularityDescending(String email, Pageable pageable, OffsetDateTime end);

    void deleteAllByIdIn(Collection<Long> informationIds);

    void deleteAllByIdAndUserEmail(Long informationId, String email);
}
