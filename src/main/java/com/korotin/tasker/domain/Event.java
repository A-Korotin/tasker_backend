package com.korotin.tasker.domain;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends Record {
    private ZonedDateTime endDate;
}
