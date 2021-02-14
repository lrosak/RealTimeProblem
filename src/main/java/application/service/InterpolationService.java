package application.service;

import application.dto.InterpolatedDataDTO;
import application.entity.Data;
import application.repository.DataRepository;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterpolationService {

    private static final String LACK_OF_DATA_BETWEEN_DATES_ERROR = "There is no data between given dates";

    private final DataRepository dataRepository;

    @Autowired
    public InterpolationService(DataRepository repository) {
        this.dataRepository = repository;
    }

    public List<InterpolatedDataDTO> findInterpolatedValuesBetweenDates(LocalDateTime from, LocalDateTime to,
                                                                        int intervalMinutes) {
        List<Data> resultData = dataRepository.findGoodDataBetweenDatesSortedByDate(from, to);
        Preconditions.checkState(!resultData.isEmpty(), LACK_OF_DATA_BETWEEN_DATES_ERROR);
        return interpolate(from, to, intervalMinutes, resultData);
    }

    private List<InterpolatedDataDTO> interpolate(LocalDateTime from, LocalDateTime to, int intervalMinutes, List<Data> resultData) {
        LocalDateTime startDate = resultData.get(0).getDate();
        LocalDateTime fromDate = prepareFromDate(from, startDate, intervalMinutes);
        LocalDateTime toDate = prepareToDate(to, Iterables.getLast(resultData), intervalMinutes);
        PolynomialSplineFunction polynomialFunction = preparePolynomialFunction(resultData);
        List<LocalDateTime> interpolatedDataTimes = prepareInterpolatedDataTimes(fromDate, toDate, intervalMinutes);
        return prepareInterpolatedData(interpolatedDataTimes, startDate, polynomialFunction);
    }

    private LocalDateTime prepareFromDate(LocalDateTime givenFrom, LocalDateTime repositoryFrom, int intervalMinutes) {
        LocalDateTime fromDate = givenFrom;
        while (fromDate.isBefore(repositoryFrom)) {
            fromDate = fromDate.plusMinutes(intervalMinutes);
        }
        return fromDate;
    }

    private LocalDateTime prepareToDate(LocalDateTime givenTo, Data repositoryToData, int intervalMinutes) {
        LocalDateTime toDate = givenTo;
        LocalDateTime repositoryTo = repositoryToData.getDate();
        while(toDate.isAfter(repositoryTo)) {
            toDate = toDate.minusMinutes(intervalMinutes);
        }
        return toDate;
    }

    private PolynomialSplineFunction preparePolynomialFunction(List<Data> data) {
        double[] times = toDatesAsDoubleArray(data);
        double[] values = toValuesArray(data);
        LinearInterpolator interpolator = new LinearInterpolator();
        return interpolator.interpolate(times, values);
    }

    private double[] toDatesAsDoubleArray(List<Data> data) {
        LocalDateTime startDate = data.get(0).getDate();
        return data.stream()
                .map(Data::getDate)
                .mapToDouble(date -> toDateAsDouble(date, startDate))
                .toArray();
    }

    private double[] toValuesArray(List<Data> data) {
        return data.stream()
                .mapToDouble(Data::getValue)
                .toArray();
    }

    private List<LocalDateTime> prepareInterpolatedDataTimes(LocalDateTime from, LocalDateTime to,
           int intervalMinutes) {
        List<LocalDateTime> interpolatedData = Lists.newArrayList();
        for (LocalDateTime current = from; !current.isAfter(to); current = current.plusMinutes(intervalMinutes)) {
            interpolatedData.add(current);
        }
        return interpolatedData;
    }

    private List<InterpolatedDataDTO> prepareInterpolatedData(List<LocalDateTime> interpolatedDataTimes, LocalDateTime startDate,
            PolynomialSplineFunction polynomialFunction) {
        return interpolatedDataTimes.stream()
                .map(interpolatedDataTime -> toInterpolatedData(interpolatedDataTime, startDate, polynomialFunction))
                .collect(Collectors.toList());
    }

    private InterpolatedDataDTO toInterpolatedData(LocalDateTime currentTime, LocalDateTime startDate,
           PolynomialSplineFunction polynomialFunction) {
        Double currentTimeAsDouble = toDateAsDouble(currentTime, startDate);
        Double value = polynomialFunction.value(currentTimeAsDouble);
        return new InterpolatedDataDTO(currentTime, value);
    }

    private double toDateAsDouble(LocalDateTime currentDate, LocalDateTime startDate) {
        Long currentDateLong = dateToLong(currentDate);
        Long startDateLong = dateToLong(startDate);
        return (double) (currentDateLong - startDateLong);
    }

    private Long dateToLong(LocalDateTime date) {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
