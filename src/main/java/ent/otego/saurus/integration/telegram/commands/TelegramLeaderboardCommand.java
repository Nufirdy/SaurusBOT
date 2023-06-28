package ent.otego.saurus.integration.telegram.commands;

import static ent.otego.saurus.integration.telegram.InlineKeyboardProcedures.getChooseTrackCategoryKeyboard;

import ent.otego.saurus.integration.ChatMessage;
import ent.otego.saurus.integration.commands.leaderboard.LeaderboardCommand;
import ent.otego.saurus.integration.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramLeaderboardCommand extends LeaderboardCommand<Update> {

    private final TelegramBot bot;

    public TelegramLeaderboardCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void executeCommand(ChatMessage<Update> message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getMessengerData().getMessage().getChatId())
                .text("Leaderboard for")
                .replyMarkup(getChooseTrackCategoryKeyboard())
                .build();
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
