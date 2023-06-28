package ent.otego.saurus.core.events;

import ent.otego.saurus.core.model.MapRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewWorldRecordEvent extends ApplicationEvent {

    private final MapRecord mapRecord;

    public NewWorldRecordEvent(MapRecord record) {
        super(record);
        mapRecord = record;
    }
}
