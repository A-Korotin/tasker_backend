package com.korotin.tasker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * One of the domain classes of Tasker application.
 * Time-bound record that can be marked as done.
 */
@Entity
@Table(name = "task")
@Data
@EqualsAndHashCode(callSuper = true)
public class Task extends Record {
    @Column(nullable = false)
    private Boolean done;
}
