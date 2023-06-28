package ent.otego.saurus.core;

import static org.mockito.Mockito.mock;

import ent.otego.saurus.core.mappers.*;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.core.testimpl.*;
import ent.otego.saurus.nadeo.NadeoCoreServices;
import ent.otego.saurus.nadeo.client.CoreServicesClient;
import jakarta.persistence.EntityManager;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.*;

@Configuration
public class CustomLeaderboardsTrackerTestConfig {

    @Bean
    @Autowired
    public CustomLeaderboardsTracker customLeaderboardsTracker(
            NadeoCoreServices coreServices,
            MapRecordMapper mapRecordMapper,
            EntityManager entityManager,
            CustomLeaderboardRepository leaderboardRepository,
            MapRecordRepository mapRecordRepository,
            PlayerAccountRepository accountRepository,
            ApplicationEventPublisher eventPublisher,
            MapInfoRepository mapInfoRepository
    ) {
        return new CustomLeaderboardsTracker(coreServices, mapRecordMapper,
                entityManager, leaderboardRepository, mapRecordRepository,
                accountRepository, mapInfoRepository, eventPublisher);
    }

    @Bean
    public NadeoCoreServices nadeoCoreServices() {
        return new NadeoCoreServicesTestImpl(mock(CoreServicesClient.class));
    }

    @Bean
    public MapRecordMapper mapRecordMapper() {
        return Mappers.getMapper(MapRecordMapper.class);
    }

    @Bean
    public EntityManager entityManager() {
        return mock(EntityManager.class);
    }

    @Bean
    public CustomLeaderboardRepository customLeaderboardRepository() {
        return new CustomLeaderboardRepositoryTestImpl();
    }

    @Bean
    public MapRecordRepository mapRecordRepository() {
        return new MapRecordRepositoryTestImpl();
    }

    @Bean
    public PlayerAccountRepository playerAccountRepository() {
        return new PlayerAccountRepositoryTestImpl();
    }
}
