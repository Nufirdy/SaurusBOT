package ent.otego.saurus.integration.telegram;

import ent.otego.saurus.core.events.*;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.integration.*;
import ent.otego.saurus.integration.model.*;
import ent.otego.saurus.integration.repository.SubscriptionsRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramEventsSender {

    private TelegramBot bot;
    private SubscriptionsRepository subscriptionsRepository;
    private MessageSource messageSource;

    @Autowired
    public TelegramEventsSender(TelegramBot bot, SubscriptionsRepository subscriptionsRepository) {
        this.bot = bot;
        this.subscriptionsRepository = subscriptionsRepository;
    }

    @EventListener
    public void sendNewWorldRecordNotification(NewWorldRecordEvent event)
            throws TelegramApiException {
        List<Subscription> worldRecordSubscriptions =
                subscriptionsRepository.findAllSubscribersBySubscriptionTypeAndPlatform(
                        SubscriptionType.NEW_WORLD_RECORD, TelegramChat.class);
        for (Subscription subscription : worldRecordSubscriptions) {
            Object[] args = {event.getMapRecord().getTimestamp(),
                    event.getMapRecord().getMapInfo().getName(),
                    MessageUtils.millisecondsToTime(event.getMapRecord().getTime()),
                    event.getMapRecord().getAccount().getDisplayName()};

            String message = messageSource.getMessage("newWorldRecord", args,
                    subscription.getSubscriber().getLocale());
            SendMessage worldRecordMessage = SendMessage.builder()
                    .chatId(((TelegramChat) subscription.getSubscriber()).getChatId())
                    .text(message)
                    .build();
            bot.execute(worldRecordMessage);
        }
    }

    @EventListener
    @Transactional
    public void sendLeaderboardStandingsEvent(LeaderboardRankingEvent event) {
        if (!(event.getSubscriber() instanceof TelegramChat chat)) {
            return;
        }
        List<MapRecord> oldLeaderboard = event.getOldLeaderboard();
        List<MapRecord> newLeaderboard = event.getNewLeaderboard();
        MapInfo mapInfo = newLeaderboard.get(0).getMapInfo();

        StringBuilder message = new StringBuilder();
        message.append(mapInfo.getName()).append("\n");
        for (int i = 1; i <= newLeaderboard.size(); i++) {
            MapRecord record = newLeaderboard.get(i - 1);
            int indexOf = oldLeaderboard.indexOf(record);
            message.append(i).append(". ");
            message.append(record.getAccount().getDisplayName()).append(" ");
            message.append(MessageUtils.millisecondsToTime(record.getTime()));
            message.append(" ");
            if (indexOf == -1) {
                message.append(Character.toChars(0x1F195));
            } else if (indexOf - i - 1 > 0) {
                message.append(Character.toChars(0x2B06));
            } else if (indexOf - i - 1 < 0) {
                message.append(Character.toChars(0x2B07));
            } else {
                message.append(Character.toChars(0x2796));
            }
            message.append("\n");
        }

        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chat.getChatId())
                .photo(new InputFile(mapInfo.getThumbnailUrl()))
                .caption(message.toString())
                .build();
        try {
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
