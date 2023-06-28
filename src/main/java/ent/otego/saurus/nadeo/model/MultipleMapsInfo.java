package ent.otego.saurus.nadeo.model;

import java.util.List;

public record MultipleMapsInfo(List<MapInfo> mapList,
                               Integer itemCount) {
}
