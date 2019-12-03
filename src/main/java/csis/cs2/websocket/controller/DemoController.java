package csis.cs2.websocket.controller;

import csis.cs2.websocket.entity.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
}
