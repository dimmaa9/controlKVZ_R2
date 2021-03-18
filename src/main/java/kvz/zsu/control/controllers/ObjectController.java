package kvz.zsu.control.controllers;

import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Scope;
import kvz.zsu.control.models.Type;
import kvz.zsu.control.models.User;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ScopeService;
import kvz.zsu.control.services.TypeService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.bouncycastle.util.Arrays;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/object")
@AllArgsConstructor
public class ObjectController {

    private final ObjectService objectService;
    private final UserService userService;
    private final ScopeService scopeService;
    private final TypeService typeService;




    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }

    @ModelAttribute("objects")
    public List<Object> objectList() {
        List<Object> objectList = objectService.findAll();
        return objectList.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).collect(Collectors.toList());
    }

    @ModelAttribute("scops")
    public List<Scope> getScops() {
        return scopeService.findAll();
    }

    @ModelAttribute("types")
    public List<Type> typeList() {
        return typeService.findAll();
    }
}
