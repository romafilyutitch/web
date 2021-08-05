package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity represents user's subscription.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class Subscription implements DbEntity {
    private final Long id;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Constructor with id generated by database.
     * Used when it's need to map database table data to instance.
     * @param id subscription id generated by database.
     * @param startDate subscription start date.
     * @param endDate subscription end date.
     */
    public Subscription(Long id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructor without id generated by database.
     * Used when it's need to save and register subscription in database table.
     * @param startDate subscription start date.
     * @param endDate subscription end date.
     */
    public Subscription(LocalDate startDate, LocalDate endDate) {
        this(null, startDate, endDate);
    }

    /**
     * Returns saved subscription id generated by database.
     * @return saves subscription id generated by database.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Returns subscription start date.
     * @return subscription start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns subscription end date.
     * @return subscription end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Checks weather two object are equal to each other.
     * @param o other object.
     * @return {@code true} if objects are equal or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    /**
     * Calculates instance hash code.
     * @return instnace hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate);
    }

    /**
     * Makes instance string representation.
     * @return instance string representation.
     */
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
