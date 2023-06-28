package ent.otego.saurus.core;

import static org.mockito.Mockito.*;

import ent.otego.saurus.core.model.MapRecord;
import ent.otego.saurus.core.repository.MapRecordRepository;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CustomLeaderboardsTrackerTestConfig.class)
public class CustomLeaderboardsTrackerTest {

    @Autowired
    CustomLeaderboardsTracker tracker;

    @Autowired
    MapRecordRepository mapRecordRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    public void updateCustomLeaderboards() {
        ArgumentCaptor<MapRecord> mapRecordCaptor = ArgumentCaptor.forClass(MapRecord.class);
        when(entityManager.merge(any())).then(invocation -> {
            MapRecord mapRecordArgument = invocation.getArgument(0);
            return mapRecordRepository.save(mapRecordArgument);
        });

        tracker.updateAllCustomLeaderboards();
        verify(entityManager, times(5)).merge(mapRecordCaptor.capture());
    }
}