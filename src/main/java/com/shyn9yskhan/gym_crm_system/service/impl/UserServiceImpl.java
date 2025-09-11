package com.shyn9yskhan.gym_crm_system.service.impl;

import com.shyn9yskhan.gym_crm_system.AppMetrics;
import com.shyn9yskhan.gym_crm_system.domain.User;
import com.shyn9yskhan.gym_crm_system.dto.UserDto;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.UserRepository;
import com.shyn9yskhan.gym_crm_system.service.RandomGenerator;
import com.shyn9yskhan.gym_crm_system.service.UserCreationResult;
import com.shyn9yskhan.gym_crm_system.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private UserRepository userRepository;
    private AppMetrics metrics;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AppMetrics metrics, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.metrics = metrics;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserCreationResult createUser(String firstname, String lastname) {
        String username = makeUniqueUsername(firstname, lastname);
        String rawPassword = RandomGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(firstname);
        userEntity.setLastname(lastname);
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(encodedPassword));
        userEntity.setActive(true);

        userRepository.save(userEntity);
        metrics.userCreated();
        userEntity.setPassword(rawPassword);
        return new UserCreationResult(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        String username = userDto.getUsername();
        String firstname = userDto.getFirstname();
        String lastname = userDto.getLastname();
        boolean isActive = userDto.isActive();

        UserEntity userEntity = getUserEntityByUsername(username);
        userEntity.setFirstname(firstname);
        userEntity.setLastname(lastname);
        userEntity.setUsername(makeUniqueUsername(firstname, lastname));
        userEntity.setActive(isActive);

        userRepository.save(userEntity);
        metrics.userUpdated();
        return userDto;
    }

    @Override
    public String deleteUser(String username) {
        boolean isExists = userRepository.existsByUsername(username);
        if (isExists) {
            long deletingResult = userRepository.deleteByUsername(username);
            if (deletingResult > 0) {
                metrics.userDeleted();
                return username;
            }
            else return null;
        }
        else return null;
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            User user = new User() {};
            user.setFirstname(userEntity.getFirstname());
            user.setLastname(userEntity.getLastname());
            user.setUsername(userEntity.getUsername());
            user.setPassword(userEntity.getPassword());
            user.setActive(userEntity.isActive());
            return user;
        }
        return null;
    }

    @Override
    public String changePassword(String username, String oldPassword, String newPassword) {
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
        if (userEntity != null) {
            if (userEntity.getPassword().equals(oldPassword)) {
                userEntity.setPassword(newPassword);
                userRepository.save(userEntity);
                return newPassword;
            }
            return null;
        }
        return null;
    }

    @Override
    public String setActive(String userId, boolean isActive) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            userEntity.setActive(isActive);
            userRepository.save(userEntity);
            return userId;
        }
        return null;
    }

    @Override
    public String setActiveByUsername(String username, boolean isActive) {
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
        if (userEntity != null) {
            userEntity.setActive(isActive);
            userRepository.save(userEntity);
            return username;
        }
        return null;
    }

    public UserEntity getUserEntityByUsername(String username) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            return userEntity;
        }
        return null;
    }

    private String makeUniqueUsername(String firstname, String lastname) {
        String base = firstname + "." + lastname;
        logger.debug("Generating unique username based on: {}", base);
        if (!userRepository.existsByUsername(base)) {
            return base;
        }

        List<String> similar = userRepository.findUsernameByUsernameStartingWith(base);
        int max = similar.stream()
                .map(u -> u.substring(base.length()))
                .map(s -> {
                    if (s.isEmpty()) return 0;
                    try { return Integer.parseInt(s); }
                    catch (NumberFormatException ex) { return 0; }
                })
                .max(Integer::compareTo)
                .orElse(0);

        String uniqueUsername = base + (max + 1);
        logger.debug("Resolved unique username: {}", uniqueUsername);
        return uniqueUsername;
    }
}
