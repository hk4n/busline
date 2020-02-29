package com.sbab.home.assignment.bus.repositories;

import com.sbab.home.assignment.bus.models.BusLine;
import com.sbab.home.assignment.bus.models.Journey;
import com.sbab.home.assignment.bus.models.NoOfJourneys;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

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
    @Transactional
    void shouldGetAll() {
        List<BusLine> result = lineRepository.findAll();
        assertThat(result.size(), Matchers.is(10));
    }

    @Test
    @Transactional
    void shouldFindAllLineNumbers() {
        List<Integer> result = lineRepository.findAllLineNumbers();
        assertThat(result.size(), Matchers.is(10));
    }

    @Test
    @Transactional
    void shouldReturn10NoOfJourneyInHighestJourneySizeOrder() {
        Page<NoOfJourneys> result = lineRepository.getBusLinesNoOfJourneysOrderUsingJourneySizeDesc(PageRequest.of(0, 10));
        assertThat(result.getTotalElements(), Matchers.is(10L));
        assertThat(result.getContent().get(0).getNoOfJourneys(), Matchers.is(10));
    }
}
