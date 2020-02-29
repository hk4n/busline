package com.sbab.home.assignment.bus.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "line")
public class BusLine {
    @Id
    int lineNumber;

    @Column
    String lineDesignation;

    @Column
    String defaultTransportMode;

    @Column
    String defaultTransportModeCode;

    @Column
    String lastModifiedUtcDateTime;

    @Column
    String existsFromDate;

    @JsonIgnore
    @OneToMany(mappedBy = "busLine", cascade = CascadeType.ALL)
    List<Journey> journeys;
}
