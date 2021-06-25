package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class Subscription implements DbEntity {
    private final Long id;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Subscription(Long id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Subscription(Long id) {
        this(id, null, null);
    }

    public Subscription(LocalDate startDate, LocalDate endDate) {
        this(null, startDate, endDate);
    }

    @Override
    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Subscription updateStartDate(LocalDate newStartDate) {
        if (newStartDate == null) {
            return this;
        }
        return new Subscription(id, newStartDate, endDate);
    }

    public Subscription updateEndDate(LocalDate newEndDate) {
        if (newEndDate == null) {
            return this;
        }
        return new Subscription(id, startDate, newEndDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
