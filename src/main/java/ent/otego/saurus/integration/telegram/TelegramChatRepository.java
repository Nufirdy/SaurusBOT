package ent.otego.saurus.integration.telegram;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {

    TelegramChat findByChatId(long chatId);
}
