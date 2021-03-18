package kvz.zsu.control.controllers;

import kvz.zsu.control.models.Unit;
import kvz.zsu.control.models.User;
import kvz.zsu.control.services.UnitService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/units")
public class UnitController {
    private final UserService userService;
    private final UnitService unitService;

    @GetMapping
    public String getUnitsTable() {
        return "units";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model, @AuthenticationPrincipal User user) {
        Unit unit = new Unit();
        unit.setParentUnit(user.getUnit());
        model.addAttribute("unit", unit);
        return "units-create";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditPage(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("edit-units");
        mav.addObject("unitEdit", unitService.findById(id));
        return mav;
    }

    @PostMapping(value = "/create", params = {"saveUnit"})
    public String save(Unit unit) {
        unitService.save(unit);
        return "redirect:/units";
    }

    @PostMapping(value = "/edit/{id}", params = {"saveUnit"})
    public String saveEditUnit(Unit unit) {
        return save(unit);
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }

    @ModelAttribute("units")
    public List<Unit> getUnits(@AuthenticationPrincipal User user) {
        return getUser(user).getUnit().getUnits();
    }
}
