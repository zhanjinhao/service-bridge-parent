package sb.dashboard.ui.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UIController {

    @RequestMapping("/")
    public String homePage(){
        return "forward:/jar.html";
    }

}
