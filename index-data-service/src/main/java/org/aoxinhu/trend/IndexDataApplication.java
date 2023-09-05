package org.aoxinhu.trend;
 
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class IndexDataApplication {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        int defaultPort = 8021;
        int redisPort = 6379;
        int eurekaServerPort = 8761;
 
        if(NetUtil.isUsableLocalPort(eurekaServerPort)) {
            System.err.printf("detect port %d isn't open, so eureka server hasn't started,thus exit %n", eurekaServerPort );
            System.exit(1);
        }
 
        if(NetUtil.isUsableLocalPort(redisPort)) {
            System.err.printf("detect port %d isn't open, so redis server hasn't started,thus exit %n", redisPort );
            System.exit(1);
        }
         
        if(null!=args && 0!=args.length) {
            for (String arg : args) {
                if(arg.startsWith("port=")) {
                    String strPort= StrUtil.subAfter(arg, "port=", true);
                    if(NumberUtil.isNumber(strPort)) {
                        port = Convert.toInt(strPort);
                    }
                }
            }
        }       
         
        // if(0==port) {
        //     Future<Integer> future = ThreadUtil.execAsync(() ->{
        //         int p = 0;
        //         System.out.printf("please enter your port in 5s, the defult port is %d %n",defaultPort);
        //         Scanner scanner = new Scanner(System.in);
        //         while(true) {
        //             String strPort = scanner.nextLine();
        //             if(!NumberUtil.isInteger(strPort)) {
        //                 System.err.println("number only");
        //                 continue;
        //             }
        //             else {
        //                 p = Convert.toInt(strPort);
        //                 scanner.close();
        //                 break;
        //             }
        //         }
        //         return p;
        //     });
        //     try{
        //         port=future.get(5,TimeUnit.SECONDS);
        //     }
        //     catch (InterruptedException | ExecutionException | TimeoutException e){
        //         port = defaultPort;
        //     }          
        // }
         
        if(!NetUtil.isUsableLocalPort(port)) {
            System.err.printf("port %d is occupied,unable to start %n", port );
            System.exit(1);
        }
        new SpringApplicationBuilder(IndexDataApplication.class).properties("server.port=" + port).run(args);
         
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
     
}