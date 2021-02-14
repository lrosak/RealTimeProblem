package application.dto;

import application.entity.Data;

import java.time.LocalDateTime;

public class RealTimeDataDTO {

    private LocalDateTime date;
    private double value;
    private QualityDTO quality;

    public static RealTimeDataDTO fromData(Data data) {
        return new RealTimeDataDTO(data);
    }

    private RealTimeDataDTO(Data data) {
        date = data.getDate();
        value = data.getValue();
        quality = QualityDTO.valueOf(data.getQuality().name());
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

    public QualityDTO getQuality() {
        return quality;
    }

    public void setQuality(QualityDTO quality) {
        this.quality = quality;
    }

    private enum QualityDTO {
        Good, Bad
    }

}
