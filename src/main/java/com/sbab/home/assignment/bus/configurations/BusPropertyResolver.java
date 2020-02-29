package com.sbab.home.assignment.bus.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class BusPropertyResolver {

    final static String path = "/api2/LineData.json";

    @Value("${host}")
    private String host;
    @Value("${key}")
    private String key;

    public String getHost() {
        return host;
    }

    public String getKey() {
        return key;
    }

    public URL getLineDataURL(String model) throws MalformedURLException {
        return UriComponentsBuilder.fromHttpUrl(getHost())
                .path(path)
                .queryParam("model", model)
                .queryParam("key", getKey())
                .build().toUri().toURL();
    }
}
