package ent.otego.saurus.core.mappers;

import ent.otego.saurus.core.model.MapInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = PlayerAccountMapper.class)
public interface MapInfoMapper {

    @Mapping(target = "id", source = "mapId")
    MapInfo mapInfoToMapInfoEntity(ent.otego.saurus.nadeo.model.MapInfo mapInfo);
}
