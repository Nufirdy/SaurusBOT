package ent.otego.saurus.integration.telegram;

import ent.otego.saurus.integration.model.Subscriber;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Entity
@Table(name = "telegram_chat")
@Getter
@Setter
@NoArgsConstructor
public class TelegramChat extends Subscriber {

    @Column(unique = true, nullable = false, updatable = false)
    private long chatId;//сделать индекс

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TelegramChat that = (TelegramChat) o;
        return chatId == that.chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chatId);
    }
}
