package com.shyn9yskhan.gym_crm_system;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AppMetrics {

    private final Counter authAttempts;
    private final Counter authSuccess;
    private final Counter authFailure;

    private final Counter userCreated;
    private final Counter userUpdated;
    private final Counter userDeleted;

    public AppMetrics(MeterRegistry registry) {
        this.authAttempts = Counter.builder("app.auth.attempts")
                .description("Number of authentication attempts")
                .register(registry);

        this.authSuccess = Counter.builder("app.auth.success")
                .description("Number of successful authentications")
                .register(registry);

        this.authFailure = Counter.builder("app.auth.failure")
                .description("Number of failed authentications")
                .register(registry);

        this.userCreated = Counter.builder("app.user.created")
                .description("Number of users created")
                .register(registry);

        this.userUpdated = Counter.builder("app.user.updated")
                .description("Number of users updated")
                .register(registry);

        this.userDeleted = Counter.builder("app.user.deleted")
                .description("Number of users deleted")
                .register(registry);
    }

    public void authAttempt() { authAttempts.increment(); }
    public void authSuccess() { authSuccess.increment(); }
    public void authFailure() { authFailure.increment(); }

    public void userCreated() { userCreated.increment(); }
    public void userUpdated() { userUpdated.increment(); }
    public void userDeleted() { userDeleted.increment(); }
}
