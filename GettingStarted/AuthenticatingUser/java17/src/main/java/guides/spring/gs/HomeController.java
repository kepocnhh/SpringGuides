package guides.spring.gs;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index() {
        return "Welcome to the home page!";
    }
}
