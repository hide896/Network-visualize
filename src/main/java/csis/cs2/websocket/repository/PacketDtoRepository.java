package csis.cs2.websocket.repository;

import csis.cs2.websocket.entity.PacketDto;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class PacketDtoRepository {

    private String visualizeTargetIpPrefix = "133.37";

    public List<PacketDto> getPacketDtoFromString(List<String> stringList) {
        if(stringList == null) {
            log.error("Received null");
            return null;
        }
        if(stringList.size() == 0) {
            log.error("Received an empty list");
            return null;
        }
        List<PacketDto> packetDtoList = new ArrayList<PacketDto>();
        for(String string : stringList) {
            if(string.isEmpty()) continue;
            String[] packetElemString = string.split(",");
            if(packetElemString.length < 4) {
                log.error("PacketListStrings contain empty element(s)");
                continue;
            }
            PacketDto tmpPacketDto = new PacketDto();
            tmpPacketDto.setSourceIp(packetElemString[0]);
            tmpPacketDto.setDestinationIp(packetElemString[1]);
            tmpPacketDto.setSourcePort(Integer.parseInt(packetElemString[2]));
            tmpPacketDto.setDestinationPort(Integer.parseInt(packetElemString[3]));
            if(packetElemString.length > 4) {
                tmpPacketDto.setStringFlag(packetElemString[4]);
            }
            if(tmpPacketDto.getDestinationIp().startsWith(visualizeTargetIpPrefix)) {
                tmpPacketDto.setVisualizeTarget(true);
            } else {
                tmpPacketDto.setVisualizeTarget(false);
            }
            packetDtoList.add(tmpPacketDto);
        }
        return packetDtoList;
    }
}
