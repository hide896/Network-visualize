package csis.cs2.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

@Slf4j
@Controller
public class UdpController {

    @ServiceActivator(inputChannel = "udpInboundChannel")
    public void handleMessage(byte[] bytes) throws UnsupportedEncodingException {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        String string = new String(packet.getData(), "UTF-8");
        log.info(string);
    }
}
