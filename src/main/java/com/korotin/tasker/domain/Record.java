package com.korotin.tasker.domain;


import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Abstract base class for all the records in Tasker application.
 * Adds description and startDate
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Record extends BaseEntity {

    private String description;

    private ZonedDateTime startDate;
}
