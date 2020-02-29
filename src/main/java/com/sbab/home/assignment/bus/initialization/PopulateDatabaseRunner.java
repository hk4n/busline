package com.sbab.home.assignment.bus.initialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbab.home.assignment.bus.configurations.BusPropertyResolver;
import com.sbab.home.assignment.bus.initialization.jsonmodels.JourneyPatternPointOnLine;
import com.sbab.home.assignment.bus.initialization.jsonmodels.JourneyResponse;
import com.sbab.home.assignment.bus.initialization.jsonmodels.LineResponse;
import com.sbab.home.assignment.bus.models.BusLine;
import com.sbab.home.assignment.bus.models.Journey;
import com.sbab.home.assignment.bus.repositories.LineRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("!test")
public class PopulateDatabaseRunner implements ApplicationRunner {

    final BusPropertyResolver properties;
    final LineRepository lineRepository;

    public PopulateDatabaseRunner(BusPropertyResolver properties, LineRepository lineRepository) {
        this.properties = properties;
        this.lineRepository = lineRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        URL lineUrl = properties.getLineDataURL("line");
        URL journeyUrl = properties.getLineDataURL("jour");

        LineResponse lineResponse = new ObjectMapper().readValue(lineUrl, LineResponse.class);
        JourneyResponse journeyResponse = new ObjectMapper().readValue(journeyUrl, JourneyResponse.class);

        // populate the database
        if (lineResponse.getStatusCode() == 0 && journeyResponse.getStatusCode() == 0) {

            List<JourneyPatternPointOnLine> filteredJourneys = journeyResponse.getResponseData().getJourneys().stream()
                    .distinct().collect(Collectors.toList());

            lineResponse.getResponseData().getLines()
                    .stream().filter(line -> "BUS".equals(line.getDefaultTransportModeCode()))
                    .forEach(line -> {
                BusLine newBusLine = new BusLine();
                newBusLine.setLineNumber(line.getLineNumber());
                newBusLine.setDefaultTransportMode(line.getDefaultTransportMode());
                newBusLine.setDefaultTransportModeCode(line.getDefaultTransportModeCode());
                newBusLine.setExistsFromDate(line.getExistsFromDate());
                newBusLine.setLastModifiedUtcDateTime(line.getLastModifiedUtcDateTime());

                List<Journey> journeys = filteredJourneys.stream()
                        .filter(jour -> jour.getLineNumber() == line.getLineNumber())
                        .map(jour -> {
                            Journey newJourney = new Journey();
                            newJourney.setLineNumber(jour.getLineNumber());
                            newJourney.setDirectionCode(jour.getDirectionCode());
                            newJourney.setExistsFromDate(jour.getExistsFromDate());
                            newJourney.setJourneyPatternPointNumber(jour.getJourneyPatternPointNumber());
                            newJourney.setLastModifiedUtcDateTime(jour.getLastModifiedUtcDateTime());
                            newJourney.setBusLine(newBusLine);
                            return newJourney;

                        }).collect(Collectors.toList());

                newBusLine.setJourneys(journeys);
                lineRepository.save(newBusLine);
            });

        }
    }

}
