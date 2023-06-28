package ent.otego.saurus.integration.telegram;

import ent.otego.saurus.core.CustomLeaderboardsService;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.integration.SubscriberService;
import ent.otego.saurus.integration.commands.Command;
import ent.otego.saurus.integration.telegram.commands.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Bean
    @Autowired
    public TelegramBot telegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String username,
            TelegramChatRepository chatRepository,
            CampaignRepository campaignRepository,
            CustomLeaderboardsService customLeaderboardsService,
            MapInfoRepository mapInfoRepository,
            Collection<Command<Update>> commands
    )
            throws TelegramApiException {
        TelegramBot telegramBot =
                new TelegramBot(botToken, username, commands, chatRepository,
                        campaignRepository, customLeaderboardsService, mapInfoRepository);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBot);

        updateBotCommands(telegramBot);
        return telegramBot;
    }

    @Bean
    @Autowired
    public TelegramStartCommand telegramStartCommand(
            TelegramChatRepository chatRepository,
            SubscriberService subscriberService,
            @Lazy TelegramBot bot
    ) {
        return new TelegramStartCommand(chatRepository, subscriberService, bot);
    }

    @Bean
    @Autowired
    public TelegramLeaderboardCommand telegramLeaderboardCommand(@Lazy TelegramBot bot) {
        return new TelegramLeaderboardCommand(bot);
    }

    @Bean
    @Autowired
    public TelegramAddPlayerCommand telegramAddPlayerCommand(
            CustomLeaderboardsService customLeaderboardsService,
            TelegramChatRepository chatRepository,
            @Lazy TelegramBot bot
    ) {
        return new TelegramAddPlayerCommand(customLeaderboardsService, chatRepository, bot);
    }

    private void updateBotCommands(TelegramBot telegramBot) throws TelegramApiException {
        BotCommand startCommand = BotCommand.builder()
                .command("start")
                .description("Activate and setup bot")
                .build();
        GetMyCommands getPrivateChatCommands = GetMyCommands.builder()
                .scope(new BotCommandScopeAllPrivateChats())
                .build();
        ArrayList<BotCommand> currentPrivateChatCommands =
                telegramBot.execute(getPrivateChatCommands);
        if (!currentPrivateChatCommands.contains(startCommand)) {
            SetMyCommands setMyCommands = SetMyCommands.builder()
                    .command(startCommand)
                    .scope(new BotCommandScopeAllPrivateChats())
                    .build();
            telegramBot.execute(setMyCommands);
        }
    }
}
