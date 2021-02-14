package application.dto;

import java.time.LocalDateTime;

public class InterpolatedDataDTO {

    private LocalDateTime date;
    private double interpolatedValue;

    public InterpolatedDataDTO() { }

    public InterpolatedDataDTO(LocalDateTime date, double interpolatedValue) {
        this.date = date;
        this.interpolatedValue = interpolatedValue;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getInterpolatedValue() {
        return interpolatedValue;
    }

    public void setInterpolatedValue(double interpolatedValue) {
        this.interpolatedValue = interpolatedValue;
    }
}
