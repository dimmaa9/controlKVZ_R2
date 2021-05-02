package kvz.zsu.control.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MvcConfig implements WebMvcConfigurer {


    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File("src/main/resources/static/avatars").getAbsolutePath().replace('\\', '/') + "/";
        registry.addResourceHandler("/avatars/**").addResourceLocations("file:///" + absolutePath);
    }

}
