package com.sbab.home.assignment.bus.initialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbab.home.assignment.bus.configurations.BusPropertyResolver;
import com.sbab.home.assignment.bus.initialization.jsonmodels.JourneyResponse;
import com.sbab.home.assignment.bus.initialization.jsonmodels.LineResponse;
import com.sbab.home.assignment.bus.models.BusLine;
import com.sbab.home.assignment.bus.repositories.LineRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 8080)
@ActiveProfiles("test")
class PopulateDatabaseRunnerTest {

    private URL lineURL;
    private URL jourURL;
    private String linePath;
    private String jourPath;

    @Autowired
    BusPropertyResolver properties;

    @Autowired
    LineRepository lineRepository;

    @BeforeEach
    void setup() throws MalformedURLException {
        lineURL = properties.getLineDataURL("line");
        jourURL = properties.getLineDataURL("jour");

        linePath = lineURL.getPath() + "?" + lineURL.getQuery();
        jourPath =jourURL.getPath() + "?" + jourURL.getQuery();

    }

    @Test
    void shouldMapToLineResponse() throws IOException {

        stubFor(get(urlEqualTo(linePath))
                .willReturn(aResponse().withBodyFile("line.json")));

        LineResponse lineResponse = new ObjectMapper().readValue(lineURL, LineResponse.class);

        assertThat(lineResponse, Matchers.notNullValue());
        assertThat(lineResponse.getResponseData().getLines().size(), Matchers.greaterThan(10));
    }

    @Test
    void shouldMapToJourneyResponse() throws IOException {

        stubFor(get(urlEqualTo(jourPath))
                .willReturn(aResponse().withBodyFile("jour.json")));

        JourneyResponse journeyResponse = new ObjectMapper().readValue(jourURL, JourneyResponse.class);

        assertThat(journeyResponse, Matchers.notNullValue());
        assertThat(journeyResponse.getResponseData().getJourneys().size(), Matchers.greaterThan(10));
    }

    @Test
    @Transactional
    void shouldPopulateDatabase() throws Exception {

        stubFor(get(urlEqualTo(jourPath))
                .willReturn(aResponse().withBodyFile("jour.json")));

        stubFor(get(urlEqualTo(linePath))
                .willReturn(aResponse().withBodyFile("line.json")));

        PopulateDatabaseRunner populateDatabaseRunner = new PopulateDatabaseRunner(properties, lineRepository);
        populateDatabaseRunner.run(new DefaultApplicationArguments());

        Optional<BusLine> finds = lineRepository.findById(1);
        assertThat(finds.orElseThrow().getJourneys().size(), Matchers.is(61));
    }

}
