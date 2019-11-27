package csis.cs2.networkvisualize.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Home {

    @GetMapping("/")
    public String home(@RequestParam(name="name", required=false, defaultValue="K") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
}
