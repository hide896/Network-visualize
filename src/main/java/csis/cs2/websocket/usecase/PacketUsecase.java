package csis.cs2.websocket.usecase;

import csis.cs2.websocket.entity.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Service
public class PacketUsecase {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public void savePackets(List<Packet> packets) throws InterruptedException {
        log.info("[START]");
        if(packets == null) {
            log.error("Received null");
            return;
        }
        if(packets.size() == 0) {
            log.error("Received an empty list");
            return;
        }
        log.debug("SIZE = {}", packets.size());

        simpMessagingTemplate.convertAndSend("/topic/packets", packets.toArray());
        log.info("[END]");
    }
}
