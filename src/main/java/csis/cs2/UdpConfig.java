package csis.cs2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;

@Slf4j
@Configuration
public class UdpConfig {
    @Bean
    public UnicastReceivingChannelAdapter udpIn() {
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(11111);
        adapter.setOutputChannelName("udpInboundChannel");
        return adapter;
    }
}