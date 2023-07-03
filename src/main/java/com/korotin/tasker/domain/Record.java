package com.korotin.tasker.domain;


import jakarta.persistence.Column;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    protected String description;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    protected ZonedDateTime startDate;
}
