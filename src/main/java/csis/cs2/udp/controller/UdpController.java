package csis.cs2.udp.controller;

import csis.cs2.websocket.entity.Packet;
import csis.cs2.websocket.entity.PacketDto;
import csis.cs2.websocket.repository.PacketDtoRepository;
import csis.cs2.websocket.repository.PacketRepository;
import csis.cs2.websocket.usecase.PacketUsecase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.*;

@Slf4j
@Controller
public class UdpController {

    @Autowired
    private DataDeliverer dataDeliverer;

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void receiveData(byte[] bytes) throws Exception {
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        dataDeliverer.deliverData(datagramPacket);
    }
}
