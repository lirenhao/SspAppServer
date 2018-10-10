package com.yada.ssp.appServer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "ssp-client")
public class SspClientProperties {

    private String hostName;
    private int port;
    private int lenSize;
    private String ver;
    private int timeOut;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getLenSize() {
        return lenSize;
    }

    public void setLenSize(int lenSize) {
        this.lenSize = lenSize;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
