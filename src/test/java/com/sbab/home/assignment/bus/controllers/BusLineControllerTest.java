package com.sbab.home.assignment.bus.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbab.home.assignment.bus.models.BusLine;
import com.sbab.home.assignment.bus.models.Journey;
import com.sbab.home.assignment.bus.models.NoOfJourneys;
import com.sbab.home.assignment.bus.repositories.LineRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BusLineControllerTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setup() {

        lineRepository.deleteAll();

        for (int i = 1; i <= 10; i++) {

            BusLine busLine = new BusLine();
            busLine.setLineNumber(i);

            List<Journey> journeys = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                Journey journey = new Journey();
                journey.setJourneyPatternPointNumber(UUID.randomUUID().hashCode());
                journey.setLineNumber(i);
                journey.setBusLine(busLine);
                journeys.add(journey);
            }

            busLine.setJourneys(journeys);
            lineRepository.save(busLine);
        }

        lineRepository.flush();
    }

    @Test
    void shouldReturnAllBusLineNumbers() throws Exception {
        MvcResult result = mvc.perform(get("/buslines")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

        List<Integer> lineNumbers = new ObjectMapper()
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Integer>>(){});

        assertThat(lineNumbers.size(), Matchers.is(10));
    }

    @Test
    void shouldReturnBusLineSpecifiedByTheLineNumber() throws Exception {
        MvcResult result = mvc.perform(get("/buslines/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BusLine busLine = new ObjectMapper()
                .readValue(result.getResponse().getContentAsByteArray(), BusLine.class);

        assertThat(busLine.getLineNumber(), Matchers.is(1));
    }

    @Test
    void shouldReturnAllJourneysRelatedToTheBusLineNumber() throws Exception {
        MvcResult result = mvc.perform(get("/buslines/10/journeys")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Journey> journeys = new ObjectMapper()
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Journey>>(){});

        assertThat(journeys.size(), Matchers.is(10));
    }

    @Test
    void shouldReturnTheTop10BusLinesOrderedByJourneySize() throws Exception {
        MvcResult result = mvc.perform(get("/search/top10/buslinesByJourneys")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Map<String, Integer>> busLines = new ObjectMapper()
                .readValue(result.getResponse().getContentAsByteArray(),
                        new TypeReference<List<Map<String, Integer>>>(){});

        assertThat(busLines.size(), Matchers.is(10));
        assertThat(busLines.get(0).get("noOfJourneys"), Matchers.is(10));
        assertThat(busLines.get(1).get("noOfJourneys"), Matchers.is(9));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mvc.perform(get("/buslines/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
