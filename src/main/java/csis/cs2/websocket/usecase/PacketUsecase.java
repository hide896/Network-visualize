package csis.cs2.websocket.usecase;

import csis.cs2.websocket.entity.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PacketUsecase {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private long lastSentTime = System.currentTimeMillis();
    private final long sendDataInterval = 250;  // 0.5秒
    private HashMap<Packet, Integer> packetsCountCollection = new HashMap<>();

    private int totalPacketCount = 0;

    @GetMapping
    public void savePackets(Set<Packet> packets) throws InterruptedException {
        log.debug("[START]");
        if(Objects.isNull(packets)) {
            log.error("Received null");
            return;
        }
        if(packets.size() == 0) {
            log.error("Received an empty list");
            return;
        }
        for(Packet tmpPacket : packets) {
            int tmpCount = packetsCountCollection.getOrDefault(tmpPacket, 0);
            packetsCountCollection.put(tmpPacket, tmpCount+1);
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastSentTime > sendDataInterval) {
            Object[] dataToSend = packetsCountCollection.entrySet().stream()
                    .filter(ip -> ip.getValue() < 3)
                    .map(ip -> ip.getKey())
                    .toArray();
            totalPacketCount += dataToSend.length;
//            log.info("Total: {} packets.", totalPacketCount);
//            log.info("obj: {}", dataToSend[0]);
            simpMessagingTemplate.convertAndSend("/topic/packets", dataToSend);
            lastSentTime = currentTime;
            packetsCountCollection.clear();
        }

        log.debug("[END]");
    }
}
