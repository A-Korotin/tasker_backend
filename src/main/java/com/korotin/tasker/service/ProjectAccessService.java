package com.korotin.tasker.service;

import com.korotin.tasker.domain.User;

import java.util.UUID;

public interface ProjectAccessService {
    boolean hasAccess(User user, UUID projectId);
}
