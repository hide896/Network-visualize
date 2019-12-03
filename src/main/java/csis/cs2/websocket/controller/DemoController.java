package csis.cs2.websocket.controller;

import csis.cs2.websocket.entity.Greeting;
import csis.cs2.websocket.entity.HelloMessage;
import csis.cs2.websocket.entity.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public void addPacket() {
        log.info("[START]");
        Packet tmp = new Packet();
        tmp.setX(new Random().nextInt(250));
        tmp.setY(new Random().nextInt(250));
        simpMessagingTemplate.convertAndSend("/topic/packets", tmp);
        log.info("[END]");
    }

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
