package com.sbab.home.assignment.bus.initialization.jsonmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class LineResponse {
    @JsonProperty("StatusCode")
    int statusCode;

    @JsonProperty("Message")
    String message;

    @JsonProperty("ExecutionTime")
    int executionTime;

    @JsonProperty("ResponseData")
    ResponseData responseData;

    @Getter
    @ToString
    @NoArgsConstructor
    public static class ResponseData {
        @JsonProperty("Version")
        String version;

        @JsonProperty("Type")
        String type;

        @JsonProperty("Result")
        List<Line> lines;
    }

}
