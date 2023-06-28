package ent.otego.saurus.core.events;

import ent.otego.saurus.core.model.MapRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PersonalBestEvent extends ApplicationEvent {

    private final MapRecord mapRecord;

    public PersonalBestEvent(MapRecord record) {
        super(record);
        mapRecord = record;
    }

    @Override
    public String toString() {
        return "PersonalBestEvent{" +
               "mapRecord=" + mapRecord +
               '}';
    }
}
