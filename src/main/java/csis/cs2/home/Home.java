package csis.cs2.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class Home {

    @GetMapping("/")
    public String home(@RequestParam(name="name", required=false, defaultValue="K") String name, Model model) {
        log.info(name);
        model.addAttribute("name", name);
        return "index";
    }
}
