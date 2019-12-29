package csis.cs2.websocket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PacketDto {
    private LocalDateTime receivedAt;
    private int protocol;
    private String sourceIp;
    private int sourcePort;
    private String destinationIp;
    private int destinationPort;
    private int flag;
    private String stringFlag;
    private long sequence;
    int payloadLength;
}
