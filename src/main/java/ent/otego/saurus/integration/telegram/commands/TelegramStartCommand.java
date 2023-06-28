package ent.otego.saurus.integration.telegram.commands;

import ent.otego.saurus.integration.*;
import ent.otego.saurus.integration.commands.start.StartCommand;
import ent.otego.saurus.integration.model.Subscriber;
import ent.otego.saurus.integration.telegram.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramStartCommand extends StartCommand<Update> {

    private final TelegramChatRepository chatRepository;
    private final TelegramBot bot;

    public TelegramStartCommand(
            TelegramChatRepository chatRepository,
            SubscriberService subscriberService,
            TelegramBot bot
    ) {
        super(subscriberService);
        this.chatRepository = chatRepository;
        this.bot = bot;
    }

    @Override
    protected Subscriber getSubscriber(Update messengerData) {
        long chatId = getChatId(messengerData);
        return chatRepository.findByChatId(chatId);
    }

    @Override
    protected Subscriber saveNewSubscriber(Update messengerData) {
        long chatId = getChatId(messengerData);
        TelegramChat chat = new TelegramChat();
        chat.setChatId(chatId);
        chat.setPrivate(isPrivateChat(messengerData));
        chatRepository.save(chat);
        return chat;
    }

    @Override
    protected void onSubscription(Update messengerData) {
        long chatId = getChatId(messengerData);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Placeholder greeting")
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResubscription(Update messengerData) {
        long chatId = getChatId(messengerData);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Placeholder greeting")
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static long getChatId(Update update) {
        if (update.hasMyChatMember()) {
            return update.getMyChatMember().getChat().getId();
        } else {
            return update.getMessage().getChatId();
        }
    }

    private static boolean isPrivateChat(Update update) {
        if (update.hasMyChatMember()) {
            return update.getMyChatMember().getChat().isUserChat();
        } else {
            return update.getMessage().getChat().isUserChat();
        }
    }
}
