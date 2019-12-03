package csis.cs2.websocket.controller;

import csis.cs2.websocket.entity.Greeting;
import csis.cs2.websocket.entity.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {
        log.info("[START]");
        Greeting tmp = new Greeting();
        tmp.setContent("Hello, " + HtmlUtils.htmlEscape(message.getName()));
        log.info("[END]");
        return tmp;
    }
}
