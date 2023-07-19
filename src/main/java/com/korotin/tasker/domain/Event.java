package com.korotin.tasker.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

/**
 * One of the domain classes of Tasker application.
 * Time-bound record that has end date and can not be marked as done.
 */
@Entity
@Table(name = "event")
@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends Record {
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private ZonedDateTime endDate;

    public DateRange createDateRange() {
        return new DateRange(this.startDate, this.endDate);
    }
}
