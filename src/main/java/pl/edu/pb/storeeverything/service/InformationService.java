package pl.edu.pb.storeeverything.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pb.storeeverything.dto.InformationDto;
import pl.edu.pb.storeeverything.dto.PageRequestDto;
import pl.edu.pb.storeeverything.dto.mapper.InformationMapper;
import pl.edu.pb.storeeverything.entity.Information;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.repository.InformationRepository;
import pl.edu.pb.storeeverything.service.utility.DateValidator;
import pl.edu.pb.storeeverything.service.utility.ModelBuilder;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    private final CategoryService categoryService;

    private final UserCategoryService userCategoryService;

    private final UserService userService;

    private final DataDeletionService dataDeletionService;

    private final InformationMapper informationMapper;

    private final DateValidator dateValidator;

    private final ModelBuilder modelBuilder;


    public Information getIfOwner(User user, Long informationId) {
        Information information = findById(informationId);

        if (!Objects.equals(user.getEmail(), information.getUser().getEmail())) {
            throw new IllegalCallerException("User with email: [%s] is not owner of the information with ID: [%s]."
                    .formatted(user.getEmail(), information.getId()));
        }

        return information;
    }

    public Information findById(Long informationId) {
        return informationRepository.findById(informationId)
                .orElseThrow(() -> new EntityNotFoundException("Information with ID: [%s] not found".formatted(informationId)));
    }

    public InformationDto getInformation(Long informationId) {
        return informationMapper.map(findById(informationId));
    }

    public Page<InformationDto> getInformation(String userEmail, PageRequestDto pageRequestDto) {
        Pageable pageable = modelBuilder.createPageable(pageRequestDto);
        Long categoryId = pageRequestDto.getCategoryId();

        if (dateValidator.hasValidDates(pageRequestDto)) {
            return getInformationByDate(userEmail, pageRequestDto, pageable, categoryId);
        }

        if ("categoryPopularity".equals(pageRequestDto.getSortField())) {
            return getInformationByCategoryPopularity(userEmail, pageable);
        }

        if (Objects.nonNull(categoryId)) {
            return informationRepository.findByUserEmailAndCategoryId(userEmail, categoryId, pageable)
                    .map(informationMapper::map);
        }

        return informationRepository.findByUserEmail(userEmail, pageable)
                .map(informationMapper::map);
    }

    @Transactional
    public void create(InformationDto informationDTO) {
        User user = userService.findById(informationDTO.getUserId());

        if(!userCategoryService.existsByEmailAndCategoryId(user.getEmail(), informationDTO.getCategoryId())) {
            throw new IllegalCallerException("The category with ID: [%s] is not assigned to the user with email: [%s]."
                    .formatted(informationDTO.getCategoryId(), user.getEmail()));
        }

            Information information = Information.builder()
                    .title(informationDTO.getTitle())
                    .content(informationDTO.getContent())
                    .link(informationDTO.getLink())
                    .user(user)
                    .category(categoryService.findById(informationDTO.getCategoryId()))
                    .creationDate(OffsetDateTime.now())
                    .build();

        informationRepository.save(information);
    }

    @Transactional
    public void deleteById(User user, Long informationId) {
        dataDeletionService.deleteSingleInformationAndAssociatedData(user.getEmail(), informationId);
    }

    @Transactional
    public void update(Long informationId, User owner, InformationDto updatedInformation) {
        Information information = getIfOwner(owner, informationId);

        information.setTitle(updatedInformation.getTitle());
        information.setContent(updatedInformation.getContent());
        information.setLink(updatedInformation.getLink());
        information.setCategory(categoryService.findById(updatedInformation.getCategoryId()));
    }

    private Page<InformationDto> getInformationByDate(String userEmail, PageRequestDto pageRequestDto, Pageable pageable, Long categoryId) {
        if (dateValidator.hasStartDate(pageRequestDto) && dateValidator.hasEndDate(pageRequestDto)) {
            OffsetDateTime from = modelBuilder.buildStartDate(pageRequestDto.getStartDate());
            OffsetDateTime to = modelBuilder.buildEndDate(pageRequestDto.getEndDate());
            return getInformation(userEmail, pageRequestDto, pageable, categoryId, from, to);
        } else if (dateValidator.hasStartDate(pageRequestDto)) {
            OffsetDateTime from = modelBuilder.buildStartDate(pageRequestDto.getStartDate());
            OffsetDateTime to = OffsetDateTime.now();

            return getInformation(userEmail, pageRequestDto, pageable, categoryId, from, to);

        } else {
            OffsetDateTime to = modelBuilder.buildEndDate(pageRequestDto.getEndDate());
            if ("categoryPopularity".equals(pageRequestDto.getSortField())) {
                return getInformationByCategoryPopularity(userEmail, pageable, to);
            }
            if (Objects.nonNull(categoryId)) {
                return informationRepository.findByUserEmailAndCategoryIdAndCreationDateBefore(userEmail, categoryId, to, pageable)
                        .map(informationMapper::map);
            }

            return informationRepository.findByUserEmailAndCreationDateBefore(userEmail, to, pageable)
                    .map(informationMapper::map);
        }
    }

    private Page<InformationDto> getInformation(String userEmail, PageRequestDto pageRequestDto, Pageable pageable, Long categoryId, OffsetDateTime from, OffsetDateTime to) {
        if ("categoryPopularity".equals(pageRequestDto.getSortField())) {
            return getInformationByCategoryPopularity(userEmail, pageable, from, to);
        }

        if (Objects.nonNull(categoryId)) {
            return informationRepository.findByUserEmailAndCategoryIdAndCreationDateBetween(userEmail, categoryId, from, to, pageable)
                    .map(informationMapper::map);
        }

        return informationRepository.findByUserEmailAndCreationDateBetween(userEmail, from, to, pageable)
                .map(informationMapper::map);
    }


    private Page<InformationDto> getInformationByCategoryPopularity(String userEmail, Pageable pageable) {
        return informationRepository.findByUserEmailSortedByCategoryPopularityDescending(userEmail, pageable)
                .map(informationMapper::map);
    }

    private Page<InformationDto> getInformationByCategoryPopularity(String userEmail, Pageable pageable, OffsetDateTime from, OffsetDateTime to) {
        return informationRepository.findByUserEmailAndDateBetweenSortedByCategoryPopularityDescending(userEmail, pageable, from, to)
                .map(informationMapper::map);
    }

    private Page<InformationDto> getInformationByCategoryPopularity(String userEmail, Pageable pageable, OffsetDateTime to) {
        return informationRepository.findByUserEmailAndDateBetweenSortedByCategoryPopularityDescending(userEmail, pageable, to)
                .map(informationMapper::map);
    }
}
