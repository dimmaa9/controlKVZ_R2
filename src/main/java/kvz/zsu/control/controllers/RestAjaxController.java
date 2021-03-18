package kvz.zsu.control.controllers;

import kvz.zsu.control.models.Thing;
import kvz.zsu.control.models.Type;
import kvz.zsu.control.models.User;
import kvz.zsu.control.services.ScopeService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.TypeService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class RestAjaxController {

    private final ScopeService scopeService;
    private final UserService userService;
    private final ThingService thingService;

    @GetMapping(value = "/getPrice", produces = "application/json")
    public String getD(@AuthenticationPrincipal User user) {
        user = userService.findById(user.getId());
        List<Thing> thing = user.getUnit().getThingList();

        List<Double> temp = new ArrayList<>();
        for (var item : thing) {
            temp.add(item.getPrice());
        }

        return new JSONObject().put("price", temp).toString();
    }

    @GetMapping(value = "/getState", produces = "application/json")
    public String getState(@AuthenticationPrincipal User user) {
        user = userService.findById(user.getId());
        List<Thing> thing = user.getUnit().getThingList();
        int vn = 0, pb = 0, rm = 0;

        for (int i = 0; i < thing.size(); i++) {
            if (thing.get(i).getState().getState().equals("В наявності"))
                vn++;
            else if (thing.get(i).getState().getState().equals("Потреба"))
                pb++;
            else if (thing.get(i).getState().getState().equals("Ремонт"))
                rm++;
        }
        return new JSONObject().put("vn", vn).put("pb", pb).put("rm", rm).toString();
    }

    @GetMapping(value = "/getStateByCategory", produces = "application/json")
    public String getStateByCategory(@AuthenticationPrincipal User user) {
        user = userService.findById(user.getId());
        return new JSONObject()
                .put("listVn", thingService.getListCountVnInCategory(user.getUnit()))
                .put("listPb", thingService.getListCountPbInCategory(user.getUnit()))
                .put("listRm", thingService.getListCountRmInCategory(user.getUnit())).toString();
    }


}
