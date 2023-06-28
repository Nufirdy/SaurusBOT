package ent.otego.saurus.core.mappers;

import ent.otego.saurus.core.model.MapRecord;
import ent.otego.saurus.nadeo.model.MapRecordDTO;
import org.mapstruct.*;

@Mapper(uses = PlayerAccountMapper.class)
public interface MapRecordMapper {

    @Mapping(target = "id", source = "mapRecordId")
    @Mapping(target = "mapInfo", ignore = true)
    @Mapping(target = "score", source = "recordScore.score")
    @Mapping(target = "time", source = "recordScore.time")
    @Mapping(target = "respawnCount", source = "recordScore.respawnCount")
    @Mapping(target = "account", ignore = true)
    MapRecord mapRecordDTOToEntity(MapRecordDTO mapRecordDto);
}
