package org.jeecg;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.jeecg.modules.message.websocket.WebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {"org.jeecg", "org.kunze","org.alipay"})
@MapperScan({"org.kunze.*.mapper"})
@EnableElasticsearchRepositories(basePackages = "org.kunze.diansh.esRepository")
@EnableAutoConfiguration
public class JeecgApplication {

    public static void main(String[] args) throws UnknownHostException {
        //System.setProperty("spring.devtools.restart.enabled", "true");
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ConfigurableApplicationContext application = SpringApplication.run(JeecgApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application Jeecg-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
        //解决WebSocket不能注入的问题
        WebSocket.setApplicationContext(application);
        log.info("WebSocket注入成功！");
    }
}