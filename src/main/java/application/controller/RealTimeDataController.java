package application.controller;

import application.dto.InterpolatedDataDTO;
import application.dto.RealTimeDataDTO;
import application.service.InterpolationService;
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

    private final RealTimeDataService realTimeDataService;
    private final InterpolationService interpolationService;

    @Autowired
    public RealTimeDataController(RealTimeDataService realTimeDataService, InterpolationService interpolationService) {
        this.realTimeDataService = realTimeDataService;
        this.interpolationService = interpolationService;
    }

    @GetMapping("/list")
    public List<RealTimeDataDTO> listAllData(){
        return realTimeDataService.list();
    }

    @GetMapping("/latest")
    public RealTimeDataDTO findLatestValue() {
        return realTimeDataService.findLatestValue();
    }

    @GetMapping("/series")
    public List<Double> findGoodValues(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return realTimeDataService.findGoodValuesBetweenDates(from, to);
    }

    @GetMapping("/average")
    public Double findAverageGoodValue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return realTimeDataService.findAverageGoodValueBetweenDates(from, to);
    }

    @GetMapping("/interpolate")
    public List<InterpolatedDataDTO> findInterpolatedValue(
            @RequestParam(name = "interval") int intervalMinutes,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return interpolationService.findInterpolatedValuesBetweenDates(from, to, intervalMinutes);
    }
}
