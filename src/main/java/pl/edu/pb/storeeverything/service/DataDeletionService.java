package pl.edu.pb.storeeverything.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.repository.*;

import java.util.List;

@Service
@AllArgsConstructor
public class DataDeletionService {
    private final CategoryRepository categoryRepository;

    private final UserCategoryRepository userCategoryRepository;

    private final InformationRepository informationRepository;

    private final InformationLinkShareRepository informationLinkShareRepository;

    private final InformationEmailShareRepository informationEmailShareRepository;


    @Transactional
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public void deleteUserCategoryAndAssociatedData(String email, Long categoryId) {
        deleteUserAllInformationAndAssociatedData(email, categoryId);
        userCategoryRepository.deleteByUserEmailAndCategoryId(email, categoryId);

        if (userCategoryRepository.countByCategoryId(categoryId).equals(0L)) {
            deleteCategoryById(categoryId);
        }
    }

    @Transactional
    public void deleteUserAllInformationAndAssociatedData(String email, Long categoryId) {
        List<Long> informationId = informationRepository.findAllInformationIdByUserEmailAndCategoryId(email, categoryId);
        deleteInformationShares(informationId);
        informationRepository.deleteAllByIdIn(informationId);
    }

    @Transactional
    public void deleteSingleInformationAndAssociatedData(String email, Long informationId) {
        deleteInformationShares(informationId);
        informationRepository.deleteAllByIdAndUserEmail(informationId, email);
    }

    @Transactional
    public void deleteInformationShares(List<Long> informationId) {
        informationEmailShareRepository.deleteAllByInformationIdIn(informationId);
        informationLinkShareRepository.deleteAllByInformationIdIn(informationId);
    }

    @Transactional
    public void deleteInformationShares(Long informationId) {
        informationEmailShareRepository.deleteByInformationId(informationId);
        informationLinkShareRepository.deleteByInformationId(informationId);
    }

    @Transactional
    public void deleteEmailShare(Long informationId, String sharedWithEmail) {
        informationEmailShareRepository.deleteByInformationIdAndUserEmail(informationId, sharedWithEmail);
    }

    @Transactional
    public void unbindEmailShare(Long shareId, String sharedWithEmail) {
        informationEmailShareRepository.deleteByIdAndUserEmail(shareId, sharedWithEmail);
    }

    @Transactional
    public void deleteLinkShare(User owner, String url) {
        informationLinkShareRepository.deleteByInformationUserEmailAndShareLink(owner.getEmail(), url);
    }
}
