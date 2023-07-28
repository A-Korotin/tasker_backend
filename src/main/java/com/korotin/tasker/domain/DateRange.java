package com.korotin.tasker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRange {
    private ZonedDateTime start;
    private ZonedDateTime end;
}
