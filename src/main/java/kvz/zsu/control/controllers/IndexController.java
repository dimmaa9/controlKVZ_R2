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
    public String getIndex(Model model, @AuthenticationPrincipal User user) {
        user = getUser(user);
        model.addAttribute("totalSum", user.getUnit().getThingList().stream().mapToDouble(Thing::getPrice).sum());
        model.addAttribute("vn", thingService.getCountThingVn(user.getUnit()));
        model.addAttribute("pb", thingService.getCountThingPb(user.getUnit()));
        model.addAttribute("rm", thingService.getCountThingRm(user.getUnit()));
        model.addAttribute("allThings", user.getUnit().getThingList().size());
        model.addAttribute("parentUnits", user.getUnit().getUnits().size());
        model.addAttribute("countPb", user.getUnit().getThingList().stream().filter(x -> x.getState().getState().equals("Потреба")).collect(Collectors.toList()).size());
        return "index";
    }


    @RequestMapping(value = "/type")
    @ResponseBody
    public Map<Long, String> getTypes(@RequestParam(value = "_scope_") Long id) {
        Map<Long, Map<Long, String>> types = typeService.getTypes(id);
        return types.get(id);
    }

    @RequestMapping(value = "/obj")
    @ResponseBody
    public Map<Long, String> getObject(@RequestParam(value = "_type_") Long id) {
        Map<Long, Map<Long, String>> objects = objectService.getObject(id);
        return objects.get(id);
    }



    @GetMapping("/generate")
    public ResponseEntity getPDF(@AuthenticationPrincipal User user) throws Exception {
        byte[] contents;
        try {

            contents = FileUtils.readFileToByteArray(new File("document.xlsx"));
            HttpHeaders headers = new HttpHeaders();

            // Here you have to set the actual filename of your pdf
            String filename = "dodatok.xlsx";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            return new ResponseEntity("Err", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
