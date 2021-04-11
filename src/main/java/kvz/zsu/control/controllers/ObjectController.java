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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/object")
@AllArgsConstructor
public class ObjectController {


    private final ObjectService objectService;
    private final UserService userService;
    private final ScopeService scopeService;
    private final TypeService typeService;

    List<Object> listObjectService;


    @GetMapping
    public String getObjectTable() {
        return "table-objects";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("object", new Object());
        return "create-object";
    }

    @GetMapping("/create/type")
    public @ResponseBody Map<Long, String> getTypes(@RequestParam("scope") Long id) {
        Map<Long, Map<Long, String>> types = typeService.getTypes(id);
        return types.get(id);
    }

    @GetMapping(value = "/create/object")
    public @ResponseBody Map<Long, String> getObject(@RequestParam(value = "type") Long id) {
        Map<Long, Map<Long, String>> objects = objectService.getObject(id);
        return objects.get(id);
    }

    @GetMapping("/delete/{id}")
    public String deleteObject(@PathVariable("id") Long id) {
        Object object = objectService.findById(id);
        if (object.getThingList().size() == 0 || object.getThingList() == null) {
            objectService.deleteById(id);
        }
        return "redirect:/object";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("object", objectService.findById(id));

        return "edit-object";
    }

    @PostMapping(value = "/save")
    public String saveObject(Object object) {
        objectService.save(object);
        return "redirect:/object";
    }

    @GetMapping("/create/scope")
    public String createScope(Model model) {
        Scope scope = new Scope();
        scope.setTypeList(new ArrayList<>());
        model.addAttribute("scope", scope);
        return "create-scope";
    }

    @PostMapping(value = "/scope/save")
    public String saveScope(String scope) {
        Scope newScope = new Scope();
        newScope.setScope(scope);
        scopeService.save(newScope);
        return "redirect:/object";
    }

    @GetMapping("/delete/scope/{id}")
    public String deleteScope(@PathVariable Long id) {
        List<Type> typeList = scopeService.findById(id).getTypeList();

        if (typeList.size() == 0) {
            scopeService.deleteById(id);
        }
        return "redirect:/object";
    }

    @GetMapping("/edit/scope/{id}")
    public ModelAndView editScope(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("edit-scope");
        mav.addObject("scope", scopeService.findById(id));

        return mav;
    }

    @PostMapping("/scope/edit/save")
    public String saveEdit(@ModelAttribute("scope") String name, @ModelAttribute("id") Long id) {
        Scope scope = scopeService.findById(id);
        scope.setScope(name);
        scopeService.save(scope);

        return "redirect:/object";
    }

    @GetMapping("/create/typeone")
    public String createType(Model model) {
        model.addAttribute("type", new Type());

        return "create-type";
    }

    @PostMapping("/type/create/save")
    public String saveType(Type type) {
        //Type type = new Type();
        //type.setType(name);
        typeService.save(type);
        //type.setScope(scopeService.findById(idScope));
        return "redirect:/object";
    }

    @GetMapping("/type/delete/{id}")
    public String deleteType(@PathVariable long id) {
        Type type = typeService.findById(id);

        if (type.getObjectList().size() == 0 || type.getObjectList() == null) {
            typeService.deleteById(id);
            return "redirect:/object";
        } else
            return "redirect:/object";
    }

    @GetMapping("/type/edit/{id}")
    public String editType(@PathVariable long id, Model model) {
        model.addAttribute("type", typeService.findById(id));

        return "edit-type";
    }

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
