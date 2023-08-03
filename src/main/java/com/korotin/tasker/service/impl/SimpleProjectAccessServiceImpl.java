package com.korotin.tasker.service.impl;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.repository.ProjectRepository;
import com.korotin.tasker.service.ProjectAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("accessService")
@RequiredArgsConstructor
public class SimpleProjectAccessServiceImpl implements ProjectAccessService {

    private final ProjectRepository projectRepository;

    @Override
    public boolean hasAccess(User user, UUID projectId) {
        UUID ownerId = projectRepository.findOwnerId(projectId);

        return user.getId().equals(ownerId);
    }
}
