package csis.cs2.websocket.controller;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.usecase.PacketUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Slf4j
//@RestController
//@RequestMapping("/demo")
public class DemoController {

//    @Autowired
//    private PacketUsecase packetUsecase;

//    @GetMapping("/at")
//    public void addAttack() throws InterruptedException {
//        log.info("[START]");
//        Thread.sleep(35000);
//        List<Packet> packets = new ArrayList<>();
//        Random random = new Random();
//        for(int i = 29; i < 129; i += random.nextInt(3)) {
//            Packet packet = new Packet(i, 126);
//            packets.add(packet);
//        }
//        packetUsecase.savePackets(packets);
//        log.info("[END]");
//    }

//    @GetMapping
//    public void addPacket() throws InterruptedException {
//        log.info("[START]");
//        Thread.sleep(10000);
//        List<Packet> packets = new ArrayList<>();
//        Random random = new Random();
//        for(int i = 0; i < 100000; ++i) {
//            Packet packet = new Packet(random.nextInt(256), random.nextInt(256));
//            packets.add(packet);
//        }
//        packetUsecase.savePackets(packets);
//        log.info("[END]");
//    }
}
