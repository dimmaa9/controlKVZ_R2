package kvz.zsu.control.controllers;

import kvz.zsu.control.services.ScopeService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RestAjaxController {

    private final ScopeService scopeService;
    private final UserService userService;
    private final ThingService thingService;


}
