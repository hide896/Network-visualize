package csis.cs2.udp;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import csis.cs2.websocket.usecase.PacketUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class UdpController {

    @Autowired
    private PacketUsecase packetUsecase;

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void handleMessage(byte[] bytes) throws UnsupportedEncodingException, InterruptedException {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        String string = new String(packet.getData(), "UTF-8");
        String[] dataArray = string.split(",");
        PacketDto packetDto = new PacketDto();
        packetDto.setSourceIp(dataArray[0]);
        packetDto.setDestinationIp(dataArray[1]);
        packetDto.setSourcePort(Integer.parseInt(dataArray[2]));
        packetDto.setDestinationPort(Integer.parseInt(dataArray[3]));
        packetDto.setStringFlag(dataArray[4]);
        // TODO: DBへの登録

        packetUsecase.savePackets((List<Packet>) getPacketFromPacketDto(packetDto));
    }


    private Packet getPacketFromPacketDto(PacketDto packetDto) {
        List<String> sourceOctets = Arrays.asList(packetDto.getSourceIp().split("."));
//        List<String> destOctets = Arrays.asList(packetDto.getDestinationIp().split("."));
        if(sourceOctets.size() < 4) return null;

        return new Packet(Integer.parseInt(sourceOctets.get(2)), Integer.parseInt(sourceOctets.get(3)));
    }
}
