package com.shyn9yskhan.gym_crm_system.service;

import com.shyn9yskhan.gym_crm_system.AppMetrics;
import com.shyn9yskhan.gym_crm_system.domain.User;
import com.shyn9yskhan.gym_crm_system.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private AppMetrics metrics;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticate_nullUsername_returnsFalse_andDoesNotCallUserService() {
        assertFalse(authenticationService.authenticate(null, "any"));
        verifyNoInteractions(userService);
    }

    @Test
    void authenticate_nullPassword_returnsFalse_andDoesNotCallUserService() {
        assertFalse(authenticationService.authenticate("someUser", null));
        verifyNoInteractions(userService);
    }

    @Test
    void authenticate_userNotFound_returnsFalse_andVerifiesCall() {
        when(userService.getUserByUsername("unknown")).thenReturn(null);
        boolean result = authenticationService.authenticate("unknown", "pw");
        assertFalse(result);
        verify(userService, times(1)).getUserByUsername("unknown");
    }

    @Test
    void authenticate_wrongPassword_returnsFalse_andVerifiesCall() {
        User user = new User() {};
        user.setUsername("John.Smith");
        user.setPassword("password");
        when(userService.getUserByUsername("John.Smith")).thenReturn(user);
        boolean result = authenticationService.authenticate("John.Smith", "password1");
        assertFalse(result);
        verify(userService, times(1)).getUserByUsername("John.Smith");
    }

    @Test
    void authenticate_correctPassword_returnsTrue_andVerifiesCall() {
        User user = new User() {};
        user.setUsername("John.Smith");
        user.setPassword("password");
        when(userService.getUserByUsername("John.Smith")).thenReturn(user);
        boolean result = authenticationService.authenticate("John.Smith", "password");
        assertTrue(result);
        verify(userService, times(1)).getUserByUsername("John.Smith");
    }
}
