package csis.cs2.websocket.usecase;

import csis.cs2.websocket.entity.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PacketUsecase {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static List<Packet> dataToSend;

    @GetMapping
    public void savePackets(List<Packet> packets) throws InterruptedException {
        log.debug("[START]");
        if(Objects.isNull(packets)) {
            log.error("Received null");
            return;
        }
        if(packets.size() == 0) {
            log.error("Received an empty list");
            return;
        }

        if(Objects.isNull(dataToSend)) {
            dataToSend = packets;
        } else {
            dataToSend.addAll(packets);
        }

        if(dataToSend.size() > 1000) {
            log.info("Sent w/ WebSocket {} packets.", dataToSend.size());
            simpMessagingTemplate.convertAndSend("/topic/packets", dataToSend.toArray());
            dataToSend.clear();
        }

        log.debug("[END]");
    }
}
