package pl.edu.pb.storeeverything.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.storeeverything.dto.InformationDto;
import pl.edu.pb.storeeverything.dto.InformationEmailShareViewDto;
import pl.edu.pb.storeeverything.dto.InformationShareDeleteDto;
import pl.edu.pb.storeeverything.dto.PageRequestDto;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.service.InformationService;
import pl.edu.pb.storeeverything.service.InformationShareService;
import pl.edu.pb.storeeverything.service.UserCategoryService;
import pl.edu.pb.storeeverything.service.utility.DateValidator;

import java.time.LocalDate;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping("/information")
public class InformationController {

    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";
    private static final String DEFAULT_PAGE_SIZE = "10";

    private final InformationService informationService;

    private final UserCategoryService userCategoryService;

    private final InformationShareService informationShareService;

    private final DateValidator dateValidator;

    @GetMapping
    public String getInformationPage(
            @AuthenticationPrincipal User user,
            @CookieValue(value = "sortFieldCookie", defaultValue = DEFAULT_SORT_FIELD) String sortFieldCookie,
            @CookieValue(value = "sortDirectionCookie", defaultValue = DEFAULT_SORT_DIRECTION) String sortDirectionCookie,
            @CookieValue(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @CookieValue(value = "categoryId", required = false) Long categoryId,
            @CookieValue(value = "startDate", required = false) String startDate,
            @CookieValue(value = "endDate", required = false) String endDate,
            Model model
    ) {
        PageRequestDto pageRequest = new PageRequestDto()
                .withSortField(sortFieldCookie)
                .withSortDirection(Sort.Direction.valueOf(sortDirectionCookie))
                .withPageSize(pageSize)
                .withCategoryId(categoryId)
                .withStartDate(startDate)
                .withEndDate(endDate);

        Page<InformationDto> page = informationService.getInformation(user.getEmail(), pageRequest);

        model.addAttribute("pageRequest", pageRequest);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("userInformation", page.getContent());
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf("ASC")));


        if(dateValidator.hasStartDate(pageRequest)) {
            model.addAttribute("startDate", LocalDate.parse(startDate));
        }

        if(dateValidator.hasEndDate(pageRequest)) {
            model.addAttribute("endDate", LocalDate.parse(endDate));
        }

        if (Objects.nonNull(categoryId)) {
            model.addAttribute("categoryId", categoryId);
        }

        return "information-view";
    }

    @PostMapping
    public String getInformationPageWithRequest(
            @Valid @ModelAttribute("pageRequest") PageRequestDto pageRequest,
            @AuthenticationPrincipal User user,
            HttpServletResponse response,
            Model model
    ) {

        if (!dateValidator.isStartDateBeforeEndDate(pageRequest)) {
            Page<InformationDto> page = Page.empty();
            model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf("ASC")));
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("userInformation", page.getContent());
            model.addAttribute("pageNumber", page.getNumber());
            model.addAttribute("dateError", "Start date is not before end date");
            return "information-view";
        }

        if (Objects.nonNull(pageRequest.getSortDirection())) {
            response.addCookie(new Cookie("sortDirectionCookie", pageRequest.getSortDirection().name()));
        }

        if (Objects.nonNull(pageRequest.getSortField())) {
            response.addCookie(new Cookie("sortFieldCookie", pageRequest.getSortField()));
        }

        if (Objects.nonNull(pageRequest.getPageSize())) {
            response.addCookie(new Cookie("pageSize", pageRequest.getPageSize().toString()));
        }

        if (dateValidator.hasStartDate(pageRequest)) {
            response.addCookie(new Cookie("startDate", pageRequest.getStartDate()));
            model.addAttribute("startDate", LocalDate.parse(pageRequest.getStartDate()));
        } else {
            response.addCookie(new Cookie("startDate", null));
        }

        if (dateValidator.hasEndDate(pageRequest)) {
            response.addCookie(new Cookie("endDate", pageRequest.getEndDate()));
            model.addAttribute("endDate", LocalDate.parse(pageRequest.getEndDate()));
        } else {
            response.addCookie(new Cookie("endDate", null));
        }

        if (Objects.nonNull(pageRequest.getCategoryId())) {
            response.addCookie(new Cookie("categoryId", pageRequest.getCategoryId().toString()));
        } else {
            response.addCookie(new Cookie("categoryId", null));
        }

        Page<InformationDto> page = informationService.getInformation(user.getEmail(), pageRequest);

        model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf("ASC")));
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("userInformation", page.getContent());
        model.addAttribute("pageNumber", page.getNumber());


        return "information-view";
    }

    @GetMapping("/shared")
    public String getSharedInformationPage(
            @AuthenticationPrincipal User user,
            @CookieValue(value = "sharedSortFieldCookie", defaultValue = "id") String sortFieldCookie,
            @CookieValue(value = "sharedSortDirectionCookie", defaultValue = "ASC") String sortDirectionCookie,
            Model model
    ) {
        PageRequestDto pageRequest = new PageRequestDto()
                .withSortField(sortFieldCookie)
                .withSortDirection(Sort.Direction.valueOf(sortDirectionCookie));

        Page<InformationEmailShareViewDto> page = informationShareService.getSharedInformation(user.getEmail(), pageRequest);

        model.addAttribute("pageRequest", pageRequest);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("userInformation", page.getContent());
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("deleteRequest", new InformationShareDeleteDto());

        return "shared-information-view";
    }

    @PostMapping("/shared")
    public String getSharedInformationPageWithRequest(
            @ModelAttribute("pageRequest") PageRequestDto pageRequestDto,
            @AuthenticationPrincipal User user,
            HttpServletResponse response,
            Model model
    ) {
        if (Objects.nonNull(pageRequestDto.getSortDirection())) {
            response.addCookie(new Cookie("sharedSortDirectionCookie", pageRequestDto.getSortDirection().name()));
        }

        if (Objects.nonNull(pageRequestDto.getSortField())) {
            response.addCookie(new Cookie("sharedSortFieldCookie", pageRequestDto.getSortField()));
        }

        Page<InformationEmailShareViewDto> page = informationShareService.getSharedInformation(user.getEmail(), pageRequestDto);

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("userInformation", page.getContent());
        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("deleteRequest", new InformationShareDeleteDto());

        return "shared-information-view";
    }

    @GetMapping("/shared/via-link/{link}")
    public String getSharedByLinkInformation(
            @PathVariable String link,
            Model model
    ) {
        InformationDto information = informationShareService.getInformationByLink(link);
        model.addAttribute("information", information);
        model.addAttribute("editPermission", informationShareService.findLinkShareById(link).getEditPermission());
        model.addAttribute("link", link);

        return "shared-via-link-view";
    }

    @GetMapping("/shared/via-link/edit/{link}")
    public String getSharedInformationEditView(
            @PathVariable String link,
            Model model
    ) {
        InformationDto informationDto = informationShareService.getInformationByLink(link);
        model.addAttribute("informationDto", informationDto);

        return "via-link-edit";
    }

    @GetMapping("/shared/viaEmail/{shareId}/edit")
    public String getSharedViaEmailEditView(
            @AuthenticationPrincipal User user,
            @PathVariable Long shareId,
            Model model
    ) {
        InformationDto informationDto = informationShareService.getInformationByShareIdAndEmail(shareId, user);
        model.addAttribute("informationDto", informationDto);
        model.addAttribute("shareId", shareId);
        return "via-email-edit";
    }

    @PutMapping("/shared/viaEmail/{shareId}/edit")
    public String editSharedViaEmailInformation(
            @AuthenticationPrincipal User user,
            @ModelAttribute("informationDto") InformationDto updatedInformation,
            @PathVariable Long shareId
    ) {
        informationShareService.updateSharedByEmailInformation(user, shareId, updatedInformation);
        return "redirect:/information/shared";
    }

    @PutMapping("/shared/via-link/edit/{link}")
    public String editSharedInformation(
            @ModelAttribute("informationDto") InformationDto updatedInformation,
            @PathVariable String link
    ) {
        informationShareService.update(link, updatedInformation);
        return "redirect:/information/shared/via-link/" + link;
    }

    @PostMapping("/add")
    public String addInformation(
            @Valid @ModelAttribute("informationDto") InformationDto informationDto,
            BindingResult result,
            @AuthenticationPrincipal User user,
            Model model,
            HttpServletResponse response
    ) {
        if (result.hasErrors()) {
            model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf("ASC")));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "addInformation";
        }
        informationService.create(informationDto.withUserId(user.getId()));
        return "redirect:/information";
    }

    @GetMapping("/add")
    public String getAddView(
            @CookieValue(value = "categorySortDir", defaultValue = "ASC") String sortDirCookie,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("informationDto", new InformationDto());
        model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf(sortDirCookie)));
        return "addInformation";
    }


    @GetMapping("/{informationId}/edit")
    public String getEditInformationView(
            @CookieValue(value = "categorySortDir", defaultValue = "ASC") String sortDirCookie,
            @AuthenticationPrincipal User user,
            @PathVariable Long informationId,
            Model model
    ) {
        model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf(sortDirCookie)));
        model.addAttribute("informationDto", informationService.getInformation(informationId));

        return "editInformation";
    }

    @PutMapping("/{informationId}/edit")
    public String editInformation(
            @Valid @ModelAttribute("informationDto") InformationDto updatedInformation,
            BindingResult result,
            @AuthenticationPrincipal User user,
            @PathVariable Long informationId,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("userCategories", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf("ASC")));
            return "editInformation";
        }

        informationService.update(informationId, user, updatedInformation);
        return "redirect:/information";
    }


    @DeleteMapping("/{informationId}/delete")
    public String deleteInformation(
            @PathVariable Long informationId,
            @AuthenticationPrincipal User user
    ) {
        informationService.deleteById(user, informationId);

        return "redirect:/information";
    }


}
