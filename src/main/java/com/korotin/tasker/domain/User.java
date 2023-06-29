package com.korotin.tasker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain entity for Tasker application user. Every user should have role.
 * Default role is {@link UserRole#USER}
 * @see UserRole
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
