package kvz.zsu.control.controllers;

import kvz.zsu.control.excel.ObjectExcelExporterImporter;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.models.Type;
import kvz.zsu.control.models.User;
import kvz.zsu.control.services.ObjectService;
import kvz.zsu.control.services.ThingService;
import kvz.zsu.control.services.TypeService;
import kvz.zsu.control.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @GetMapping("/generate")
    public void exportToExcel (HttpServletResponse response) throws IOException, InvalidFormatException {
        DateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String fileName = "attachement; filename=" + "dodatok_" + dataFormat.format(new Date()) + ".xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", fileName);

        Map<String, List<String>> map = new HashMap<>();
        List<Type> typeList = typeService.findAll();
        System.out.println(typeList.toString());
        for (Type type : typeList) {

            List<String> list = new ArrayList<>();
            var objectList = objectService.findAll();

            for (Object object : objectList) {
                if (object.getType().equals(type)) {
                    list.add(object.getObjectName());
                }
            }

            map.put(type.getTypeName(), list);
        }

        ObjectExcelExporterImporter excelExporter = new ObjectExcelExporterImporter(map);
        excelExporter.export(response);
    }

    @GetMapping("/test")
    public String getTest() {
        return "test/test";
    }



    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
