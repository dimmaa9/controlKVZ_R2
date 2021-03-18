package kvz.zsu.control.controllers;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.User;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.TypeService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class IndexController {

    private final UserService userService;
    private final ThingService thingService;
    private final ObjectService objectService;
    private final TypeService typeService;

    @GetMapping("/")
    public String getIndex() {

        return "index";
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
