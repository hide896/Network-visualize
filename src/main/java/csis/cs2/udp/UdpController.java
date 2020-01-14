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
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class UdpController {

    @Autowired
    private PacketUsecase packetUsecase;

    @Autowired
    private PacketDtoRepository packetDtoRepository;

    private String targetPacketIpPrefix = "133.37";

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void handleMessage(byte[] bytes) throws UnsupportedEncodingException, InterruptedException {
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        String wholePacketData = new String(datagramPacket.getData(), "UTF-8");
        List<String> stringPacketList = Arrays.asList(wholePacketData.split("\n"));
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

        // 描画対象のパケットのみ抽出
        List<PacketDto> targetPacketDtoList = packetDtoList.stream()
                .filter(packetDto -> packetDto.getDestinationIp().startsWith(targetPacketIpPrefix))
                .collect(Collectors.toList());

        List<Packet> packetList = getPacketFromPacketDto(targetPacketDtoList);
        log.info("Packet count = {}", packetList.size());
        packetUsecase.savePackets(packetList);
    }


    // TODO: PacketRepositoryとして移動
    private List<Packet> getPacketFromPacketDto(List<PacketDto> packetDtoList) {
        List<Packet> packetList = new ArrayList<Packet>();
        for(PacketDto packetDto : packetDtoList) {
            String destIp = packetDto.getDestinationIp();
            if( !destIp.startsWith(targetPacketIpPrefix) ) {
                continue;
            }
            String[] destIpOctets = destIp.split("\\.");
            if(destIpOctets.length < 4) continue;
            Packet tmpPacket = new Packet(Integer.parseInt(destIpOctets[3]), Integer.parseInt(destIpOctets[2]));    // x座標: 第4オクテット, y座標: 第3オクテット
            packetList.add(tmpPacket);
        }
        return packetList;
    }
}
