package com.sbab.home.assignment.bus.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface NoOfJourneys {

    int getLineNumber();

    @JsonIgnore
    List<Journey> getJourneys();

    default int getNoOfJourneys() {
        return  getJourneys().size();
    }
}
