package com.yada.ssp.appServer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "query-status")
public class QueryStatusProperties {

    private List<String> success = new ArrayList<>();
    private List<String> waiting = new ArrayList<>();
    private List<String> failed = new ArrayList<>();

    public List<String> getSuccess() {
        return success;
    }

    public void setSuccess(List<String> success) {
        this.success = success;
    }

    public List<String> getWaiting() {
        return waiting;
    }

    public void setWaiting(List<String> waiting) {
        this.waiting = waiting;
    }

    public List<String> getFailed() {
        return failed;
    }

    public void setFailed(List<String> failed) {
        this.failed = failed;
    }
}
