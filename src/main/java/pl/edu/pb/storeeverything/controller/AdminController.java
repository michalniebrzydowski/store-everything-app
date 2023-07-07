package pl.edu.pb.storeeverything.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.storeeverything.dto.PageRequestDto;
import pl.edu.pb.storeeverything.dto.UserDto;
import pl.edu.pb.storeeverything.dto.mapper.UserMapper;
import pl.edu.pb.storeeverything.entity.Role;
import pl.edu.pb.storeeverything.service.RoleService;
import pl.edu.pb.storeeverything.service.UserService;

import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private final UserMapper userMapper;

    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";
    private static final String DEFAULT_PAGE_SIZE = "10";

    @GetMapping("/users")
    public String getUsers(
            @CookieValue(value = "user_sortFieldCookie", defaultValue = DEFAULT_SORT_FIELD) String sortFieldCookie,
            @CookieValue(value = "user_sortDirectionCookie", defaultValue = DEFAULT_SORT_DIRECTION) String sortDirectionCookie,
            @CookieValue(value = "user_pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            Model model
    ) {
        PageRequestDto pageRequest = new PageRequestDto()
                .withSortField(sortFieldCookie)
                .withSortDirection(Sort.Direction.valueOf(sortDirectionCookie))
                .withPageSize(pageSize);


        Page<UserDto> page = userService.getUsers(pageRequest);

        model.addAttribute("users", page.getContent());
        model.addAttribute("pageRequest", pageRequest);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageNumber", page.getNumber());

        return "users-management";
    }

    @PostMapping("/users")
    public String getUsersWithRequest(
            @Valid @ModelAttribute("pageRequest") PageRequestDto pageRequest,
            HttpServletResponse response,
            Model model
    ) {

        if (Objects.nonNull(pageRequest.getSortDirection())) {
            response.addCookie(new Cookie("user_sortDirectionCookie", pageRequest.getSortDirection().name()));
        }

        if (Objects.nonNull(pageRequest.getSortField())) {
            response.addCookie(new Cookie("user_sortFieldCookie", pageRequest.getSortField()));
        }

        if (Objects.nonNull(pageRequest.getPageSize())) {
            response.addCookie(new Cookie("user_pageSize", pageRequest.getPageSize().toString()));
        }

        Page<UserDto> page = userService.getUsers(pageRequest);

        model.addAttribute("users", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageNumber", page.getNumber());

        return "users-management";
    }

    @GetMapping("/users/{userId}/edit")
    public String getUserEdit (
            @PathVariable Long userId,
            Model model
    ) {
        UserDto userDto = userMapper.map(userService.findById(userId));
        List<Role> allRoles = roleService.findAll();

        model.addAttribute("user", userDto);
        model.addAttribute("roles", allRoles);
        return "user-edit";
    }

    @PutMapping("/users/{userId}/edit")
    public String editUser(
            @PathVariable Long userId,
            @ModelAttribute("user") UserDto updatedUserDto
    ) {
        userService.update(userId, updatedUserDto);

        return "redirect:/admin/users";
    }

}
