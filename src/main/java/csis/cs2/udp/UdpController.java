package csis.cs2.udp;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import csis.cs2.websocket.repository.PacketDtoRepository;
import csis.cs2.websocket.usecase.PacketUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class UdpController {

    @Autowired
    private PacketUsecase packetUsecase;

    @Autowired
    private PacketDtoRepository packetDtoRepository;

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void handleMessage(byte[] bytes) throws UnsupportedEncodingException, InterruptedException {
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        String wholePacketData = new String(datagramPacket.getData(), "UTF-8");
        List<String> stringPacketList = Arrays.asList(wholePacketData.split("\n"));
        List<PacketDto> packetDtoList = packetDtoRepository.getPacketDtoFromString(stringPacketList)
        if(packetDtoList == null) {
            log.error("PacketDtoList is null");
            return;
        }
        if(packetDtoList.isEmpty()) {
            log.error("PacketDtoList is empty");
            return;
        }
        // TODO: DBへの登録
        List<Packet> packetList = getPacketFromPacketDto(packetDtoList);
        packetUsecase.savePackets(packetList);
    }


    private List<Packet> getPacketFromPacketDto(List<PacketDto> packetDtoList) {
        List<Packet> packetList = new ArrayList<Packet>();
        for(PacketDto packetDto : packetDtoList) {
            String[] sourceOctets = packetDto.getSourceIp().split("\\.");
            if(sourceOctets.length < 4) continue;
            Packet tmpPacket = new Packet(Integer.parseInt(sourceOctets[2]), Integer.parseInt(sourceOctets[3]));
            packetList.add(tmpPacket);
        }
        return packetList;
    }
}
