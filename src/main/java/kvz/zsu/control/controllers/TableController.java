package kvz.zsu.control.controllers;

import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/table")
@AllArgsConstructor
public class TableController {

    private final UserService userService;
    private final ThingService thingService;
    private final ScopeService scopeService;
    private final TypeService typeService;
    private final ObjectService objectService;
    private final StateService stateService;

    @GetMapping
    public String getTable(Model model) {
        model.addAttribute("things", thingService.findAll());
        return "tables";
    }

    @GetMapping("/create")
    public String getCreate(Model model) {
        model.addAttribute("thing", thingService.createThing());
        return "create-thing";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user) {
        if (thingService.findById(id).getUnit().getId().equals(user.getUnit().getId())) {
            thingService.deleteById(id);
            return "redirect:/table";
        } else {
            return "redirect:/table";
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editPage(@PathVariable(name = "id") Long id, @AuthenticationPrincipal User user) {
        ModelAndView mav = new ModelAndView("edit-thing");
        Thing thing = thingService.findById(id);

        if (thing.getUnit().getId().equals(user.getUnit().getId())) {
            mav.addObject("thing", thing);
            return mav;
        } else {
            return new ModelAndView("redirect:/table");
        }
    }


    //Create RequestMapping
    @RequestMapping(value = "/create", params = {"addThing"})
    public ModelAndView addThing(Thing thing) {
        ModelAndView mv = new ModelAndView("create-thing");
        mv.addObject("thing", thingService.addThing(thing));
        return mv;
    }

    @RequestMapping(value = "/create", params = {"removeThing"})
    public ModelAndView removeRowInCreate(Thing thing, HttpServletRequest req) {
        ModelAndView mv = new ModelAndView("create-thing");
        mv.addObject("thing", thingService.removeThing(thing, Long.valueOf(req.getParameter("removeThing"))));
        return mv;
    }

    @RequestMapping(value = "/create", params = {"save"})
    public String saveThing(Thing thing, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            return "redirect:/table";
        }

        thing.setUnit(user.getUnit());
        thing.getThings().forEach(x -> {
            x.setUnit(user.getUnit());
            x.setParentThing(thing);
            thingService.save(x);
        });
        thingService.save(thing);
        return "redirect:/table";
    }

    //Edit RequestMapping
    @RequestMapping(value = "/edit/{id}", params = {"addThing"})
    public ModelAndView addThing(@PathVariable(name = "id") Long id, Thing thing) {
        ModelAndView mv = new ModelAndView("edit-thing");
        mv.addObject("thing", thingService.addThing(thing));
        return mv;
    }

    @RequestMapping(value = "/edit/{id}", params = {"removeThing"})
    public ModelAndView removeRowInEdit(Thing thing, HttpServletRequest req) {
        ModelAndView mv = new ModelAndView("edit-thing");
        Thing thingRm = thing.getThings().get(Integer.parseInt(req.getParameter("removeThing")));
        mv.addObject("thing", thingService.removeThing(thing, Long.valueOf(req.getParameter("removeThing"))));
        thingService.deleteById(thingRm.getId());
        return mv;
    }

    @RequestMapping(value = "/edit/{id}", params = {"save"})
    public String saveThingEdit(Thing thing, BindingResult bindingResult, @AuthenticationPrincipal User user) {
        //return saveThing(thing, bindingResult, user);
        if (bindingResult.hasErrors()) {
            return "redirect:/table";
        }
        thing.setUnit(user.getUnit());
        thing.getThings().forEach(x -> {
            x.setUnit(user.getUnit());
            thingService.save(x);
        });
        thingService.save(thing);
        return "redirect:/table";
    }


    //ModelAttribute
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

    @ModelAttribute("states")
    public List<State> stateList() {
        return stateService.findAll();
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
