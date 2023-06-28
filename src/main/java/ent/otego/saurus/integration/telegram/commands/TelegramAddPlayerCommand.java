package ent.otego.saurus.integration.telegram.commands;

import ent.otego.saurus.core.CustomLeaderboardsService;
import ent.otego.saurus.core.model.PlayerAccount;
import ent.otego.saurus.integration.commands.addPlayer.AddPlayerCommand;
import ent.otego.saurus.integration.model.Subscriber;
import ent.otego.saurus.integration.telegram.*;
import java.util.UUID;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramAddPlayerCommand extends AddPlayerCommand<Update> {

    private final TelegramChatRepository chatRepository;
    private final TelegramBot bot;

    public TelegramAddPlayerCommand(
            CustomLeaderboardsService customLeaderboardsService,
            TelegramChatRepository chatRepository,
            TelegramBot bot
    ) {
        super(customLeaderboardsService);
        this.chatRepository = chatRepository;
        this.bot = bot;
    }

    @Override
    protected Subscriber getSubscriber(Update messengerData) {
        long chatId = messengerData.getMessage().getChatId();
        return chatRepository.findByChatId(chatId);
    }

    @Override
    protected UUID getAccountId(Update messengerData) {
        String[] commandAndArgs = messengerData.getMessage().getText().split(" ");
        return UUID.fromString(commandAndArgs[1]);
    }

    @Override
    protected void playerAddedToLeaderboard(
            Update messengerData,
            PlayerAccount playerAccount
    ) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(messengerData.getMessage().getChatId())
                .text("Player "
                      + playerAccount.getDisplayName()
                      + " was added to chat leaderboard")
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void leaderboardAlreadyHasPlayer(
            Update messengerData,
            PlayerAccount playerAccount
    ) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(messengerData.getMessage().getChatId())
                .text("Player "
                      + playerAccount.getDisplayName()
                      + " is already on leaderboard")
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void playerDoesNotExist(Update messengerData) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(messengerData.getMessage().getChatId())
                .text("Not a valid player account id. You can find your account id in profile"
                      + " tab of Trackmania settings")
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
