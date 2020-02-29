package com.sbab.home.assignment.bus.repositories;

import com.sbab.home.assignment.bus.models.BusLine;
import com.sbab.home.assignment.bus.models.NoOfJourneys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LineRepository extends JpaRepository<BusLine, Integer> {

    @Query(value = "select c from BusLine c " +
            " Order By size(c.journeys) desc")
    Page<NoOfJourneys> getBusLinesNoOfJourneysOrderUsingJourneySizeDesc(Pageable pageable);

    @Query("select c.lineNumber from BusLine c")
    List<Integer> findAllLineNumbers();

}
