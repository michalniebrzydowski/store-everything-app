package pl.edu.pb.storeeverything.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.pb.storeeverything.dto.UserRegisterDto;
import pl.edu.pb.storeeverything.service.UserService;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
        return "registration-form";
    }

    @PostMapping("/save")
    public String processRegistrationForm(
            @Valid @ModelAttribute("userRegisterDto") UserRegisterDto userRegisterDto,
            BindingResult result,
            HttpServletResponse response
    ) {
        if(result.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "registration-form";
        }
        userService.register(userRegisterDto);
        return "redirect:/login";
    }

}
