package csis.cs2.websocket.repository;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Repository
public class PacketRepository {

    public Set<Packet> getPacketFromPacketDto(List<PacketDto> packetDtoList) {
        if(Objects.isNull(packetDtoList)) {
            log.debug("Received null");
            return null;
        }
        if(packetDtoList.isEmpty()) {
            log.debug("Received an empty list");
            return null;
        }
        Set<Packet> packetSet = new HashSet<Packet>();
        for(PacketDto packetDto : packetDtoList) {
            String destIp = packetDto.getDestinationIp();
            if( !packetDto.isVisualizeTarget() ) {
                continue;
            }
            String[] destIpOctets = destIp.split("\\.");
            if(destIpOctets.length < 4) continue;
            packetSet.add(new Packet(Integer.parseInt(destIpOctets[3]), Integer.parseInt(destIpOctets[2])));   // x座標: 第4オクテット, y座標: 第3オクテット
        }
        return packetSet;
    }
}
