package application.repository;

import application.entity.Data;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DataRepository extends CrudRepository<Data, LocalDateTime> {

    @Query("from Data where date = (select max(date) from Data)")
    Data findLatest();

    @Query("from Data where date >= :from and date <= :to and quality like 'Good'")
    List<Data> findGoodDataBetweenDates(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("from Data where date >= :from and date <= :to and quality like 'Good' order by date")
    List<Data> findGoodDataBetweenDatesSortedByDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
