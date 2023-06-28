package ent.otego.saurus.core.mappers;

import ent.otego.saurus.core.model.PlayerAccount;
import java.util.UUID;
import org.mapstruct.*;

@Mapper
public interface PlayerAccountMapper {

    @Mapping(target = "id", source = "accountId")
    PlayerAccount from(UUID accountId);
}
