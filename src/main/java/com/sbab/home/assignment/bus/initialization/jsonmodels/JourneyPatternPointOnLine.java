package com.sbab.home.assignment.bus.initialization.jsonmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
@NoArgsConstructor
public class JourneyPatternPointOnLine {

    @JsonProperty("LineNumber")
    int lineNumber;

    @JsonProperty("DirectionCode")
    String directionCode;

    @JsonProperty("JourneyPatternPointNumber")
    int journeyPatternPointNumber;

    @JsonProperty("LastModifiedUtcDateTime")
    String lastModifiedUtcDateTime;

    @JsonProperty("ExistsFromDate")
    String existsFromDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JourneyPatternPointOnLine that = (JourneyPatternPointOnLine) o;
        return getJourneyPatternPointNumber() == that.getJourneyPatternPointNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJourneyPatternPointNumber());
    }
}
