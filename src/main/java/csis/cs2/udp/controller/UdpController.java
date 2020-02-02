package csis.cs2.udp.controller;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import csis.cs2.websocket.repository.PacketDtoRepository;
import csis.cs2.websocket.repository.PacketRepository;
import csis.cs2.websocket.usecase.PacketUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class UdpController {

    @Autowired
    private PacketUsecase packetUsecase;

    @Autowired
    private PacketDtoRepository packetDtoRepository;

    @Autowired
    private PacketRepository packetRepository;

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void handleMessage(byte[] bytes) throws UnsupportedEncodingException, InterruptedException {
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        String wholePacketData = new String(datagramPacket.getData(), "UTF-8");
        List<String> stringPacketList = Arrays.asList(wholePacketData.split("\n"));
        log.info("String lines count : {}", stringPacketList.size());
        List<PacketDto> packetDtoList = packetDtoRepository.getPacketDtoFromString(stringPacketList);
        if(packetDtoList == null) {
            log.error("PacketDtoList is null");
            return;
        }
        if(packetDtoList.isEmpty()) {
            log.error("PacketDtoList is empty");
            return;
        }
        // TODO: DBへの登録
        log.info("PacketDtoList size: {}", packetDtoList.size());
        // 描画対象のパケットのみ抽出
//        List<PacketDto> targetPacketDtoList = packetDtoList.stream()
//                .filter(packetDto -> packetDto.isVisualizeTarget())
//                .collect(Collectors.toList());

        List<Packet> packetList = packetRepository.getPacketFromPacketDto(packetDtoList);
        log.debug("Packet count = {}", packetList.size());
        packetUsecase.savePackets(new HashSet<Packet>(packetList));
    }
}
