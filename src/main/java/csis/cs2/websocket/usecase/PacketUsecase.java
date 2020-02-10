package csis.cs2.websocket.usecase;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.repository.DataToSendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
//@Scope("prototype")
public class PacketUsecase {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private DataToSendRepository dataToSendRepository;

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
        dataToSendRepository.addData(new ArrayList<>(packets));
        long currentTime = System.currentTimeMillis();
        List<Packet> tmpList = dataToSendRepository.getCurrent();
        // sending packets per 1000 ms or 1000 packets
        if(CollectionUtils.isEmpty(tmpList)) return;
        if(currentTime - dataToSendRepository.getLastSentTime() > 1000 || tmpList.size() > 10000) {
            dataToSendRepository.clearData();
            sendPackets(tmpList, currentTime);
        }
        log.debug("[END]");
    }

    public void sendPackets(List<Packet> dataToSend, long currentTime) {
        if(Objects.isNull(dataToSend)) return;
        simpMessagingTemplate.convertAndSend("/topic/packets", dataToSend.toArray());
        dataToSendRepository.updateCount(dataToSend.size());
        dataToSendRepository.setLastSentTime(currentTime);
        log.info("Total packet count: {}", dataToSendRepository.getTotalPacketCount());
    }


}
