package pl.edu.pb.storeeverything.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.storeeverything.dto.CategoryDto;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.service.UserCategoryService;

@Controller
@AllArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final UserCategoryService userCategoryService;

    @GetMapping
    private String getCategories(
            @CookieValue(value = "categorySortDir", defaultValue = "ASC") String sortDirCookie,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("sortDir", sortDirCookie);
        model.addAttribute("categoryList", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf(sortDirCookie)));
        return "category-view";
    }

    @PostMapping("/sort")
    public String changeSortDirection(
            @RequestParam("sortDirection") String sortDirection,
            HttpServletResponse response
    ) {
        response.addCookie(new Cookie("categorySortDir", sortDirection));
        return "redirect:/category";
    }

    @PostMapping("/add")
    public String addCategory(
            @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
            BindingResult result,
            @AuthenticationPrincipal User user,
            Model model,
            @CookieValue(value = "categorySortDir", defaultValue = "ASC") String sortDirCookie,
            HttpServletResponse response
    ) {
        if (result.hasErrors()) {
            model.addAttribute("categoryList", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf(sortDirCookie)));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "category-view";
        }

        try {
            userCategoryService.addCategory(user.getEmail(), categoryDto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("addError", e.getMessage());
            model.addAttribute("categoryList", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf(sortDirCookie)));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "category-view";
        } catch (IllegalStateException e) {
            model.addAttribute("addError", e.getMessage());
            model.addAttribute("categoryList", userCategoryService.getUserCategories(user.getEmail(), Sort.Direction.valueOf(sortDirCookie)));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "category-view";
        }

        return "redirect:/category";
    }

    @DeleteMapping("/delete/{categoryId}")
    public String deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId
    ) {
        userCategoryService.deleteUserCategoryAndAssociatedData(user.getEmail(), categoryId);

        return "redirect:/category";
    }
}

