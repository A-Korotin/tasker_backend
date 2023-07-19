package com.korotin.tasker.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * One of the domain classes of Tasker application.
 * Represents a container with records. Should be owned by certain user.
 * @see Record
 * @see User
 */
@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany
    @JoinColumn(name = "project_id")
    private List<Event> events;
}
