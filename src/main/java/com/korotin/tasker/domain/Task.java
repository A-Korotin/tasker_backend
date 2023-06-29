package com.korotin.tasker.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * One of the domain classes of Tasker application.
 * Time-bound record that can be marked as done.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Task extends Record {
    private Boolean done;
}
