package csis.cs2.websocket.repository;

import csis.cs2.websocket.entity.Packet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Data
@Component
@Slf4j
public class DataToSendRepository {
    private long lastSentTime;
    private Deque<List<Packet>> dataToSend;
    private int totalPacketCount;
    private boolean flag;

    @PostConstruct
    public void init() {
        this.lastSentTime = System.currentTimeMillis();
        this.dataToSend = new ArrayDeque<>();
        dataToSend.offerLast(new ArrayList<Packet>());
        this.totalPacketCount = 0;
        this.flag = true;
    }

    public void addData(List<Packet> packets) {
        this.dataToSend.peekLast().addAll(packets);
    }

    public List<Packet> getCurrent() {
        return this.dataToSend.peekLast();
    }

    public void updateCountAndClearData() {
        this.dataToSend.offerLast(new ArrayList<>());
        totalPacketCount += dataToSend.pollFirst().size();
    }
}
