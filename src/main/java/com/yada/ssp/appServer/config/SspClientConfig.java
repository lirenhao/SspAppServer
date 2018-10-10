package com.yada.ssp.appServer.config;

import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.ssp.appServer.net.SspPkgSplitterFactory;
import com.yada.ssp.appServer.net.SspClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class SspClientConfig {

    @Bean
    public SspClient sspClient(SspClientProperties properties) {
        InetSocketAddress hostAddress = new InetSocketAddress(properties.getHostName(), properties.getPort());
        IPackageSplitterFactory pkgSplitterFactory = new SspPkgSplitterFactory(properties.getLenSize(), properties.getVer());
        return new SspClient(hostAddress, pkgSplitterFactory, properties.getTimeOut());
    }
}
