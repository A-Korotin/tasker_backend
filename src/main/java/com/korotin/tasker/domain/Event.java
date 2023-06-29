package com.korotin.tasker.domain;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

/**
 * One of the domain classes of Tasker application.
 * Time-bound record that has end date and can not be marked as done.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends Record {
    private ZonedDateTime endDate;
}
