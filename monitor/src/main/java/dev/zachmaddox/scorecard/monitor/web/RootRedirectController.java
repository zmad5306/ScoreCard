package dev.zachmaddox.scorecard.monitor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping({"/", ""})
    public String redirectRoot() {
        return "redirect:/scorecard/list";
    }
}
