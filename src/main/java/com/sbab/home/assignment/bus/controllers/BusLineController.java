package com.sbab.home.assignment.bus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbab.home.assignment.bus.models.BusLine;
import com.sbab.home.assignment.bus.models.Journey;
import com.sbab.home.assignment.bus.models.NoOfJourneys;
import com.sbab.home.assignment.bus.repositories.LineRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Api(description = "Bus line information and statistics")
public class BusLineController {

    private final LineRepository lineRepository;

    public BusLineController(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @GetMapping("buslines")
    @Transactional
    @ApiOperation("Returns a list of all bus lines available")
    List<Integer> getAllBusLineIds() {
        return lineRepository.findAllLineNumbers();
    }

    @GetMapping("buslines/{lineNumber}")
    @Transactional
    @ApiOperation("Finds bus lines by line number")
    BusLine getBusLineById(@PathVariable Integer lineNumber) {
        return lineRepository.findById(lineNumber).orElseThrow();
    }

    @GetMapping("buslines/{lineNumber}/journeys")
    @Transactional
    @ApiOperation("Finds a bus lines journeys by line number")
    List<Journey> getBusLineJourneysById(@PathVariable Integer lineNumber) {
        return lineRepository.findById(lineNumber).orElseThrow().getJourneys();
    }

    @GetMapping("search/top10/buslinesByJourneys")
    @Transactional
    @ApiOperation("Finds top 10 bus lines with the most bus stops")
    List<NoOfJourneys> top10BusLinesByJourneys() {
        return lineRepository.getBusLinesNoOfJourneysOrderUsingJourneySizeDesc(PageRequest.of(0, 10)).getContent();
    }

    @SneakyThrows
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorJson = new ObjectMapper().writeValueAsString(Collections.singletonMap("error", e.getLocalizedMessage()));

        if (e instanceof NoSuchElementException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body((errorJson));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body((errorJson));
        }
    }

}
