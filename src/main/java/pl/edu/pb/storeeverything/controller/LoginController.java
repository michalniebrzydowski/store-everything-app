package pl.edu.pb.storeeverything.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.edu.pb.storeeverything.entity.User;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String getInformationPage(
            @AuthenticationPrincipal User user
    ) {
        if (Objects.nonNull(user)) {
            return "redirect:/information";
        }

        return "login-view";
    }
}
