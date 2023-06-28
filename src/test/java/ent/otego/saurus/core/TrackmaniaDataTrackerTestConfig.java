package ent.otego.saurus.core;

import static org.mockito.Mockito.mock;

import jakarta.persistence.EntityManager;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

@Configuration
public class TrackmaniaDataTrackerTestConfig {

    @Bean
    public EntityManager entityManager() {
        return mock(EntityManager.class);
    }


}
