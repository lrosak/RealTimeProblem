package application.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Data {

    @Id
    private LocalDateTime date;

    @Column(nullable = false)
    private double value;

    @Column(length = 4, nullable = false)
    @Enumerated(EnumType.STRING)
    private Quality quality;

    public Data () { }

    public Data(LocalDateTime date, Double value, Quality quality) {
        this.date = date;
        this.value = value;
        this.quality = quality;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public enum Quality {
        Good, Bad
    }
}
