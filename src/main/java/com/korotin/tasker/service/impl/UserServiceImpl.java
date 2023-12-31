package com.korotin.tasker.service.impl;

import com.korotin.tasker.domain.User;
import com.korotin.tasker.exception.NotFoundException;
import com.korotin.tasker.repository.UserRepository;
import com.korotin.tasker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder encoder;

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User update(UUID id, User value) {
        value.setId(id);
        value.setPassword(encoder.encode(value.getPassword()));
        return userRepository.save(value);
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id '%s' could not be found".formatted(id));
        }
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User with email '%s' could not be found".formatted(username)));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        user.setAuthorities(authorities);

        return user;
    }
}
