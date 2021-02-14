package application.init;

import application.entity.Data;
import application.entity.Data.Quality;
import application.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataLoader implements ApplicationRunner {

    private static final LocalDateTime FROM_DATE = LocalDateTime.of(2020, 2, 14, 13, 10);
    private static final LocalDateTime TO_DATE = LocalDateTime.of(2020, 2, 14, 15, 20);
    private static final int MINUTES_INTERVAL = 5;

    private static final Random random = new Random();

    private final DataRepository dataRepository;

    private double lastValue = 0.0;

    @Autowired
    public DataLoader(DataRepository repository) {
        dataRepository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (LocalDateTime current = FROM_DATE; current.isBefore(TO_DATE); current = current.plusMinutes(MINUTES_INTERVAL)) {
            prepareData(current);
        }
    }

    private void prepareData(LocalDateTime date) {
        lastValue = lastValue + random.nextInt(10) + random.nextDouble();
        Quality quality = generateQuality();
        Data record = new Data(date, lastValue, quality);
        dataRepository.save(record);
    }

    private Quality generateQuality() {
        if (random.nextInt(10) < 8) {
            return Quality.Good;
        }
        return Quality.Bad;
    }
}
