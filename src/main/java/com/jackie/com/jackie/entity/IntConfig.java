package com.jackie.com.jackie.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="int")
@PropertySource(value = "classpath:azureauth.properties_int.properties")
public class IntConfig {
    private String subscription;
    private String client;
    private String key;
    private String tenant;
    private String managementURI;
    private String baseURL;
    private String authURL;
    private String graphURL;

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getManagementURI() {
        return managementURI;
    }

    public void setManagementURI(String managementURI) {
        this.managementURI = managementURI;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getAuthURL() {
        return authURL;
    }

    public void setAuthURL(String authURL) {
        this.authURL = authURL;
    }

    public String getGraphURL() {
        return graphURL;
    }

    public void setGraphURL(String graphURL) {
        this.graphURL = graphURL;
    }
}
