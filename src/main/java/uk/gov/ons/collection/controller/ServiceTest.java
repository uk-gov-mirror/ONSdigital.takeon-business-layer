package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceTest {

    @Autowired
    DiscoveryClient discoveryClient;

    @RequestMapping("/service")
    public List<String> services() {
        return this.discoveryClient.getServices();
    }
}
