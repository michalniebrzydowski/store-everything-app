package pl.edu.pb.storeeverything.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pb.storeeverything.entity.InformationLinkShare;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InformationLinkShareRepository extends JpaRepository<InformationLinkShare, Long> {
    Optional<InformationLinkShare> findByShareLink(String link);

    List<InformationLinkShare> findByInformationId(Long informationId);

    void deleteAllByInformationIdIn(Collection<Long> informationId);

    void deleteByInformationId(Long informationId);

    void deleteByInformationUserEmailAndShareLink(String ownerEmail, String url);
}
