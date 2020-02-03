package csis.cs2.udp.controller;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import csis.cs2.websocket.repository.PacketDtoRepository;
import csis.cs2.websocket.repository.PacketRepository;
import csis.cs2.websocket.usecase.PacketUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
//@Scope("prototype")
public class DataDeliverer {

    @Autowired
    private PacketUsecase packetUsecase;

    @Autowired
    private PacketDtoRepository packetDtoRepository;

    @Autowired
    private PacketRepository packetRepository;

    public void deliverData(DatagramPacket datagramPacket) throws Exception {
        String wholePacketData = new String(datagramPacket.getData(), "UTF-8");
        List<String> stringPacketList = Arrays.asList(wholePacketData.split("\n"));
        List<PacketDto> packetDtoList = packetDtoRepository.getPacketDtoFromString(stringPacketList);
        if(packetDtoList == null) {
            log.error("packetDtoList is null");
            return;
        }
        if(packetDtoList.isEmpty()) {
            log.error("packetDtoList is empty");
            return;
        }
        // TODO: DBへの登録

        Set<Packet> packetSet = packetRepository.getPacketFromPacketDto(packetDtoList);
        if(Objects.isNull(packetSet)) {
            log.error("packetSet is null");
            return;
        }
        if(packetSet.isEmpty()) {
            log.error("packetSet is empty");
            return;
        }
        log.debug("Packet set size = {}", packetSet.size());
        packetUsecase.savePackets(packetSet);
    }
}
