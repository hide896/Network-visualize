package csis.cs2.udp;

import csis.cs2.websocket.entity.PacketDto;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.packet.IllegalRawDataException;
import org.pcap4j.packet.IpV4Packet;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.Inet4Address;

@Slf4j
@Controller
public class UdpController {

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void handleMessage(byte[] bytes) throws UnsupportedEncodingException {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        String string = new String(packet.getData(), "UTF-8");
        String[] dataArray = string.split(",");
        PacketDto packetDto = new PacketDto();
        packetDto.setSourceIp(dataArray[0]);
        packetDto.setDestinationIp(dataArray[1]);
        packetDto.setSourcePort(Integer.parseInt(dataArray[2]));
        packetDto.setDestinationPort(Integer.parseInt(dataArray[3]));
        packetDto.setStringFlag(dataArray[4]);
        log.info(String.valueOf(packetDto));
    }
}
