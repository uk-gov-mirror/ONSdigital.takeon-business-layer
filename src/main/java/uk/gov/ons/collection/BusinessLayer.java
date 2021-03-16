package uk.gov.ons.collection;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Log4j2
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
@RibbonClient(name = "business-layer")
public class BusinessLayer {
    
    public static void main(String[] args) {

        try {
            log.info("Before instantiating Spring Boot application");
            SpringApplication.run(BusinessLayer.class, args);
            log.info("Successfully instantiated the Spring Boot application");
        } catch (Exception exp) {
            log.fatal("There is a problem in running Business Layer Spring Boot application");
        }
    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate template = null;
        try {
            template = new RestTemplate();
        } catch (Exception exp) {
            log.fatal("There is a problem in instantiating SpringBoot RestTemplate which is important " +
                    "of Webservice API calls");
        }
        return template;
    }

}
