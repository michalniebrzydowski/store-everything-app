package pl.edu.pb.storeeverything.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pb.storeeverything.dto.*;
import pl.edu.pb.storeeverything.dto.mapper.InformationEmailShareMapper;
import pl.edu.pb.storeeverything.dto.mapper.InformationLinkShareMapper;
import pl.edu.pb.storeeverything.dto.mapper.InformationMapper;
import pl.edu.pb.storeeverything.entity.Information;
import pl.edu.pb.storeeverything.entity.InformationEmailShare;
import pl.edu.pb.storeeverything.entity.InformationLinkShare;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.repository.InformationEmailShareRepository;
import pl.edu.pb.storeeverything.repository.InformationLinkShareRepository;
import pl.edu.pb.storeeverything.service.utility.ModelBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InformationShareService {
    private final InformationEmailShareRepository emailShareRepository;

    private final InformationLinkShareRepository linkShareRepository;

    private final InformationService informationService;

    private final UserService userService;

    private final DataDeletionService dataDeletionService;

    private final InformationMapper informationMapper;

    private final InformationLinkShareMapper informationLinkShareMapper;

    private final InformationEmailShareMapper informationEmailShareMapper;


    private final ModelBuilder modelBuilder;

    public Page<InformationEmailShareViewDto> getSharedInformation(String userEmail, PageRequestDto pageRequestDto) {
        Pageable pageable = modelBuilder.createPageable(pageRequestDto);

        return emailShareRepository.findByUserEmail(userEmail, pageable)
                .map(informationMapper::map);
    }

    public Information findInformationByLink(String link) {
        return linkShareRepository.findByShareLink(link)
                .map(InformationLinkShare::getInformation)
                .orElseThrow(() -> new EntityNotFoundException("Information with share link: [%s] not found".formatted(link)));
    }

    public InformationDto getInformationByLink(String link) {
        return informationMapper.map(findInformationByLink(link));
    }

    public List<InformationLinkShareDto> getLinkShares(User user, Long informationId) {
        Information information = informationService.getIfOwner(user, informationId);

        return linkShareRepository.findByInformationId(information.getId()).stream()
                .map(informationLinkShareMapper::map)
                .toList();
    }

    public List<InformationEmailShareDto> getEmailShares(User user, Long informationId) {
        Information information = informationService.getIfOwner(user, informationId);

        return emailShareRepository.findByInformationId(information.getId()).stream()
                .map(informationEmailShareMapper::map)
                .toList();
    }

    public InformationLinkShare findLinkShareById(String link) {
        return linkShareRepository.findByShareLink(link)
                .orElseThrow(() -> new EntityNotFoundException("Information with share link: [%s] not found".formatted(link)));
    }

    public InformationDto getInformationByShareIdAndEmail(Long shareId, User user) {
        return informationMapper.map(findByIdAndEmail(shareId, user.getEmail()).getInformation());
    }

    public InformationEmailShare findByIdAndEmail(Long shareId, String email) {
        return emailShareRepository.findByIdAndUserEmail(shareId, email)
                .orElseThrow(() -> new EntityNotFoundException("Information share with email: [%s] and share ID: [%s] not found"
                        .formatted(email, shareId)));
    }

    public InformationEmailShare findByInformationIdAndEmail(Long informationId, String email) {
        return emailShareRepository.findByInformationIdAndUserEmail(informationId, email)
                .orElseThrow(() -> new EntityNotFoundException("Information share with email: [%s] and information ID: [%s] not found"
                        .formatted(email, informationId)));
    }

    @Transactional
    public void shareByEmail(User owner, InformationEmailShareDto request) {
        User sharedWith = userService.findByEmail(request.getUserEmail());
        Information information = informationService.getIfOwner(owner, request.getInformationId());

        if (emailShareRepository.existsByUserEmailAndInformationId(request.getUserEmail(), information.getId())) {
            InformationEmailShare share = findByInformationIdAndEmail(information.getId(), request.getUserEmail());
            Boolean editPermissionFromRequest = Optional.ofNullable(request.getEditPermission()).orElse(Boolean.FALSE);
            modifyEditPermission(request, information, share, editPermissionFromRequest);
        } else {
            createEmailShare(request, sharedWith, information);
        }
    }

    private void createEmailShare(InformationEmailShareDto request, User sharedWith, Information information) {
        InformationEmailShare informationEmailShare = InformationEmailShare.builder()
                .information(information)
                .user(sharedWith)
                .editPermission(Objects.isNull(request.getEditPermission()) ? Boolean.FALSE : request.getEditPermission())
                .build();

        emailShareRepository.saveAndFlush(informationEmailShare);
    }

    private void modifyEditPermission(InformationEmailShareDto request, Information information, InformationEmailShare share, Boolean editPermissionFromRequest) {
        if (!share.getEditPermission().equals(editPermissionFromRequest)) {
            share.setEditPermission(editPermissionFromRequest);
        } else {
            throw new EntityExistsException("Sharing entry with information ID: [%s] and email: [%s] already exists."
                    .formatted(information.getId(), request.getUserEmail()));
        }
    }

    @Transactional
    public String shareByLink(User user, InformationLinkShareDto request) {
        Information information = informationService.getIfOwner(user, request.getInformationId());

        InformationLinkShare share = InformationLinkShare.builder()
                .information(information)
                .editPermission(request.getEditPermission())
                .shareLink(UUID.randomUUID().toString())
                .build();

        return linkShareRepository.saveAndFlush(share)
                .getShareLink();
    }

    @Transactional
    public void update(String link, InformationDto updatedInformation) {
        InformationLinkShare share = findLinkShareById(link);
        if (!share.getEditPermission()) {
            throw new IllegalStateException("Information with share link: [%s] cannot be edited".formatted(link));
        }

        Information information = share.getInformation();
        information.setTitle(updatedInformation.getTitle());
        information.setContent(updatedInformation.getContent());
        information.setLink(updatedInformation.getLink());
    }

    @Transactional
    public void updateSharedByEmailInformation(User user, Long shareId, InformationDto updatedInformation) {
        InformationEmailShare share = findByIdAndEmail(shareId, user.getEmail());
        if (!share.getEditPermission()) {
            throw new IllegalStateException("Information with ID: [%s] cannot be edited by user with email: [%s]"
                    .formatted(updatedInformation.getId(), user.getEmail()));
        }

        Information information = share.getInformation();
        information.setTitle(updatedInformation.getTitle());
        information.setContent(updatedInformation.getContent());
        information.setLink(updatedInformation.getLink());
    }

    @Transactional
    public void deleteEmailShare(InformationShareDeleteDto deleteRequest) {
        dataDeletionService.deleteEmailShare(
                deleteRequest.getInformationId(),
                deleteRequest.getEmail()
        );
    }

    @Transactional
    public void unbindEmailShare(InformationShareDeleteDto deleteRequest) {
        dataDeletionService.unbindEmailShare(
                deleteRequest.getInformationId(),
                deleteRequest.getEmail()
        );
    }

    @Transactional
    public void deleteLinkShare(User owner, String url) {
        dataDeletionService.deleteLinkShare(owner, url);
    }
}
