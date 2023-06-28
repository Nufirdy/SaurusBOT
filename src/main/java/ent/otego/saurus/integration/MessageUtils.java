package ent.otego.saurus.integration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MessageUtils {

    public static String millisecondsToTime(int time) {
        Duration duration = Duration.ofMillis(time);
        if (duration.toHoursPart() != 0) {
            return String.format("%02d:%02d:%02d.%02d",
                    duration.toHoursPart(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart(),
                    duration.toMillisPart());
        } else if (duration.toMinutesPart() != 0) {
            return String.format("%02d:%02d.%02d",
                    duration.toMinutesPart(),
                    duration.toSecondsPart(),
                    duration.toMillisPart());
        }
        return String.format("%02d.%02d",
                duration.toSecondsPart(),
                duration.toMillisPart());
    }
}
