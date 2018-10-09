package com.yada.ssp.appServer;

import com.yada.sdk.net.TcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PreDestroy;
import java.io.IOException;

@SpringBootApplication
@EnableCaching
public class ServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerApplication.class).run(args);
    }

    private final TcpClient tcpClient;

    @Autowired
    public ServerApplication(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void run(String... args) throws IOException {
        tcpClient.open();
    }

    @PreDestroy
    public void exit() {
        tcpClient.close();
        System.out.println("tcp client is closed!");
    }
}
