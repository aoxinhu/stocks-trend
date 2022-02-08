package org.aoxinhu.trend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
  
import cn.hutool.core.util.NetUtil;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
      
    public static void main(String[] args) {
        // 8761 this port is defult, don't change it! 
        // later, all the sub-project would visit this port
        int port = 8761;
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("port %d is occupied, can't be start up%n", port );
            System.exit(1);
        }
        new SpringApplicationBuilder(EurekaServerApplication.class).properties("server.port=" + port).run(args);
    }
}