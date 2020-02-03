package csis.cs2.udp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;

import java.net.DatagramPacket;

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
