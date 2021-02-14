package application.controller;

import application.dto.RealTimeDataDTO;
import application.service.RealTimeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class RealTimeDataController {

    private final RealTimeDataService service;

    @Autowired
    public RealTimeDataController(RealTimeDataService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<RealTimeDataDTO> listAllData(){
        return service.list();
    }

    @GetMapping("/latest")
    public RealTimeDataDTO findLatestValue() {
        return service.findLatestValue();
    }

    @GetMapping("/series")
    public List<Double> findGoodValues(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.findGoodValuesBetweenDates(from, to);
    }

    @GetMapping("/average")
    public Double findAverageGoodValue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.findAverageGoodValueBetweenDates(from, to);
    }
}
