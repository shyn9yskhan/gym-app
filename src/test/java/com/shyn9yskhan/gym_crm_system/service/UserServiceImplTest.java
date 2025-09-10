package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.AppMetrics;
import com.shyn9yskhan.gym_crm_system.entity.UserEntity;
import com.shyn9yskhan.gym_crm_system.repository.UserRepository;
import com.shyn9yskhan.gym_crm_system.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppMetrics metrics;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void createUser_whenBaseIsFree_savesWithBaseUsername() {
        String firstname = "John";
        String lastname = "Doe";
        String base = firstname + "." + lastname;

        when(userRepository.existsByUsername(base)).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserCreationResult result = service.createUser(firstname, lastname);

        assertNotNull(result);
        UserEntity saved = result.userEntity();
        assertNotNull(saved);
        assertEquals(base, saved.getUsername());
        assertNotNull(saved.getPassword()); // generated password exists

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(metrics, times(1)).userCreated();
    }

    @Test
    void createUser_whenBaseExists_generatesSuffixAndSaves() {
        String firstname = "John";
        String lastname = "Doe";
        String base = firstname + "." + lastname;

        when(userRepository.existsByUsername(base)).thenReturn(true);
        when(userRepository.findUsernameByUsernameStartingWith(base))
                .thenReturn(List.of("John.Doe", "John.Doe1", "John.Doe2"));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserCreationResult result = service.createUser(firstname, lastname);
        assertNotNull(result);
        UserEntity saved = result.userEntity();
        assertNotNull(saved);
        assertEquals("John.Doe3", saved.getUsername());

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(metrics, times(1)).userCreated();
    }

    @Test
    void getUserByUsername_whenFound_returnsDomainUserMapped() {
        String username = "alice.smith";
        UserEntity ue = new UserEntity();
        ue.setFirstname("Alice");
        ue.setLastname("Smith");
        ue.setUsername(username);
        ue.setPassword("pw");
        ue.setActive(true);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(ue));
        var user = service.getUserByUsername(username);

        assertNotNull(user);
        assertEquals("Alice", user.getFirstname());
        assertEquals("Smith", user.getLastname());
        assertEquals(username, user.getUsername());
        assertEquals("pw", user.getPassword());
        assertTrue(user.isActive());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByUsername_whenNotFound_returnsNull() {
        when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());
        assertNull(service.getUserByUsername("nope"));
        verify(userRepository, times(1)).findByUsername("nope");
    }

    @Test
    void changePassword_whenUserExists_andOldMatches_updatesAndReturnsNewPassword() {
        String username = "john.doe";
        String oldPassword = "old";
        String newPassword = "new-pass-123";

        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setPassword(oldPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(ue));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        String returned = service.changePassword(username, oldPassword, newPassword);

        assertEquals(newPassword, returned);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertEquals(newPassword, captor.getValue().getPassword());
    }

    @Test
    void changePassword_whenUserExists_butOldDoesNotMatch_returnsNullAndDoesNotSave() {
        String username = "john.doe";
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setPassword("different-old");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(ue));

        String returned = service.changePassword(username, "old", "new");
        assertNull(returned);
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_whenUserNotFound_returnsNull() {
        when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());
        assertNull(service.changePassword("nope", "a", "b"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void setActive_whenUserExists_updatesAndReturnsUserId() {
        String userId = "u-2";
        UserEntity ue = new UserEntity();
        ue.setId(userId);
        ue.setActive(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(ue));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        String result = service.setActive(userId, true);

        assertEquals(userId, result);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertTrue(captor.getValue().isActive());
    }

    @Test
    void setActive_whenUserNotFound_returnsNull() {
        when(userRepository.findById("nope")).thenReturn(Optional.empty());
        assertNull(service.setActive("nope", true));
        verify(userRepository, never()).save(any());
    }
}
