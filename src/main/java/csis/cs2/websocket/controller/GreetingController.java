package csis.cs2.websocket.controller;

import csis.cs2.websocket.entity.Greeting;
import csis.cs2.websocket.entity.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greeting")
    public Greeting greeting(HelloMessage message) {
        Greeting tmp = new Greeting();
        tmp.setContent("Hello, " + HtmlUtils.htmlEscape(message.getName()));
        return tmp;
    }
}
