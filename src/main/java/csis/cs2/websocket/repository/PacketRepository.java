package csis.cs2.websocket.repository;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class PacketRepository {

    public List<Packet> getPacketFromPacketDto(List<PacketDto> packetDtoList) {
        List<Packet> packetList = new ArrayList<Packet>();
        for(PacketDto packetDto : packetDtoList) {
            String destIp = packetDto.getDestinationIp();
            if( !packetDto.isVisualizeTarget() ) {
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
