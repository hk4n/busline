package com.sbab.home.assignment.bus.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "journey")
public class Journey {
    @Id
    int journeyPatternPointNumber;

    @Column
    int lineNumber;

    @Column
    String directionCode;


    @Column
    String lastModifiedUtcDateTime;

    @Column
    String existsFromDate;

    @JsonIgnore
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "line_lineNumber")
    private BusLine busLine;
}
