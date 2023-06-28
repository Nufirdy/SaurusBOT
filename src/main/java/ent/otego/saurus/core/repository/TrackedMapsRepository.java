package ent.otego.saurus.core.repository;

import ent.otego.saurus.core.model.TrackedMap;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackedMapsRepository extends JpaRepository<TrackedMap, UUID> {
}
