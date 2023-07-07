package pl.edu.pb.storeeverything.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pb.storeeverything.entity.InformationEmailShare;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InformationEmailShareRepository extends JpaRepository<InformationEmailShare, Long> {

    Page<InformationEmailShare> findByUserEmail(String email, Pageable pageable);

    List<InformationEmailShare> findByInformationId(Long informationId);

    Optional<InformationEmailShare> findByIdAndUserEmail(Long informationId, String email);

    Optional<InformationEmailShare> findByInformationIdAndUserEmail(Long informationId, String email);

    boolean existsByUserEmailAndInformationId(String userEmail, Long informationId);

    void deleteByInformationIdAndUserEmail(Long informationId, String userEmail);

    void deleteByIdAndUserEmail(Long shareId, String userEmail);

    void deleteAllByInformationIdIn(Collection<Long> informationIds);

    void deleteByInformationId(Long informationId);
}
