package application.service;

import application.dto.RealTimeDataDTO;
import application.entity.Data;
import application.repository.DataRepository;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RealTimeDataService {

    private static final String LACK_OF_DATA_BETWEEN_DATES_ERROR = "There are no data between given dates";

    private final DataRepository repository;

    @Autowired
    public RealTimeDataService(DataRepository repository) {
        this.repository = repository;
    }

    public List<RealTimeDataDTO> list()  {
        List<Data> resultData = Lists.newArrayList(repository.findAll());
        return resultData.stream()
                .map(RealTimeDataDTO::fromData)
                .collect(Collectors.toList());
    }

    public RealTimeDataDTO findLatestValue() {
        return RealTimeDataDTO.fromData(repository.findLatest());
    }

    public double findAverageGoodValueBetweenDates(LocalDateTime from, LocalDateTime to) {
        List<Data> resultData = repository.findGoodDataBetweenDates(from, to);
        return calculateAverageValue(resultData);
    }

    private Double calculateAverageValue(Collection<Data> data) {
        return data.stream()
                .mapToDouble(Data::getValue)
                .average()
                .orElseThrow(() -> new IllegalStateException(LACK_OF_DATA_BETWEEN_DATES_ERROR));
    }

    public List<Double> findGoodValuesBetweenDates(LocalDateTime from, LocalDateTime to) {
        List<Data> resultData = repository.findGoodDataBetweenDates(from, to);
        return resultData.stream()
                .map(Data::getValue)
                .collect(Collectors.toList());
    }

}
