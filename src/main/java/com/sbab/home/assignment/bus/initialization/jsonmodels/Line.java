package com.sbab.home.assignment.bus.initialization.jsonmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class Line {
    @JsonProperty("LineNumber")
    int lineNumber;
    @JsonProperty("LineDesignation")
    String lineDesignation;
    @JsonProperty("DefaultTransportMode")
    String defaultTransportMode;
    @JsonProperty("DefaultTransportModeCode")
    String defaultTransportModeCode;
    @JsonProperty("LastModifiedUtcDateTime")
    String lastModifiedUtcDateTime;
    @JsonProperty("ExistsFromDate")
    String existsFromDate;
}
