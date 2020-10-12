package info.dmind.dmind.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DmindController {
    @GetMapping("/index")
    public String index(){
        return "index.html";
    }
}
