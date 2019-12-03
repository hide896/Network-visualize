package csis.cs2.websocket.controller;

import csis.cs2.websocket.entity.Greeting;
import csis.cs2.websocket.entity.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public void greeting(@RequestBody HelloMessage message) {
        log.info("[START]");
        log.info("message.name = {}", message.getName());
        Greeting tmp = new Greeting();
        tmp.setContent("Hello, " + HtmlUtils.htmlEscape(message.getName()));
        simpMessagingTemplate.convertAndSend("/topic/greetings", tmp);
        log.info("[END]");
    }
}
