package kvz.zsu.control.controllers;

import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/table")
@AllArgsConstructor
public class TableController {

    private final UserService userService;
    private final ThingService thingService;
    private final ScopeService scopeService;
    private final TypeService typeService;
    private final ObjectService objectService;

    @GetMapping
    public String getTable(Model model) {
        model.addAttribute("values", thingService.staffing());

        return "tables";
    }


    //ModelAttribute
    @ModelAttribute("things")
    public List<Thing> getThings() {
        return thingService.findAll();
    }

    @ModelAttribute("scops")
    public List<Scope> scopeList() {
        return scopeService.findAll();
    }

    @ModelAttribute("types")
    public List<Type> typeList() {
        return typeService.findAll();
    }

    @ModelAttribute("objects")
    public List<Object> objectList() {
        return objectService.findAll();
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
