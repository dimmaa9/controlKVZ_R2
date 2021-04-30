package kvz.zsu.control.controllers;

import kvz.zsu.control.models.User;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final UserService userService;

    @GetMapping
    public String getAccountPage() {

        return "account/account-profile";
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
