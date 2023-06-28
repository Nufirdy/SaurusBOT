package ent.otego.saurus.integration.telegram;

import static ent.otego.saurus.integration.telegram.InlineKeyboardProcedures.*;

import ent.otego.saurus.core.CustomLeaderboardsService;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.integration.*;
import ent.otego.saurus.integration.commands.*;
import ent.otego.saurus.integration.commands.start.StartCommand;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final static String COMMAND_TOKEN = "/";

    private final Map<String, Command<Update>> commands;

    private final TelegramChatRepository chatRepository;
    private final CustomLeaderboardsService customLeaderboardsService;
    private final MapInfoRepository mapInfoRepository;

    private final String username;

    private List<Campaign> officialCampaigns;

    public TelegramBot(
            String botToken,
            String username,
            Collection<Command<Update>> commands,
            TelegramChatRepository chatRepository,
            CampaignRepository campaignRepository,
            CustomLeaderboardsService customLeaderboardsService,
            MapInfoRepository mapInfoRepository
    ) {
        super(botToken);
        this.commands = commands.stream().collect(Collectors.toMap(Command::getName, command -> command));
        this.username = username;
        this.chatRepository = chatRepository;
        this.customLeaderboardsService = customLeaderboardsService;

        officialCampaigns =
                campaignRepository.findAllAndFetchPlaylistEager();
        this.mapInfoRepository = mapInfoRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.startsWith(COMMAND_TOKEN)) {
                    processCommand(update);
                }
            }
        }
        if (update.hasCallbackQuery()) {
            processCallbackQuery(update);
        }

        if (update.hasMyChatMember()) {
            ChatMemberUpdated myChatMemberUpdated = update.getMyChatMember();
            ChatMember myOldChatMember = myChatMemberUpdated.getOldChatMember();
            ChatMember myNewChatMember = myChatMemberUpdated.getNewChatMember();
            if (myOldChatMember.getStatus().equals("kicked")
                || myOldChatMember.getStatus().equals("left")
                   && myNewChatMember.getStatus().equals("member")) {
                Command<Update> startCommand = commands.get(StartCommand.COMMAND_NAME);
                ChatMessage<Update> chatMessage = new ChatMessage<>(update);
                startCommand.executeCommand(chatMessage);
            }
        }
    }

    private void processCommand(Update update) {
        String[] commandAndArgs = update.getMessage().getText().split(" ");
        int indexOfAt = commandAndArgs[0].indexOf('@');
        String commandString;
        if (indexOfAt != -1) {
            commandString = commandAndArgs[0].substring(1, indexOfAt);
        } else {
            commandString = commandAndArgs[0].substring(1);
        }

        Command<Update> command = commands.get(commandString);
        if (command == null) {
            return;
        }

        ChatMessage<Update> chatMessage = new ChatMessage<>(update);
        command.executeCommand(chatMessage);
    }

    public void processCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String[] splitQueryData = callbackQuery.getData().split(" ");

        switch (splitQueryData[0]) {
            case "campaigns":
                onCampaignsButton(update);
                break;
            case "customs":
                onCustomsButton(update);
                break;
            case "track_categories":
                onBackToCategoriesButton(update);
                break;
            case "page":
                onChangeCampaignsPageButton(update, splitQueryData[1]);
                break;
            case "campaign":
                onCampaignButton(update, splitQueryData[1]);
                break;
            case "track":
                onTrackButton(update, splitQueryData[1]);
                break;
        }
    }

    private void onCampaignsButton(Update update) {
        EditMessageText editMessage = EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text("Choose campaign")
                .replyMarkup(getCampaignsKeyboard(officialCampaigns, 0))
                .build();
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void onCustomsButton(Update update) {
        EditMessageText editMessage = EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text("TBD")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(getCallbackButton("..", "track_categories")))
                        .build())
                .build();
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void onBackToCategoriesButton(Update update) {
        EditMessageText editMessage = EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text("Leaderboard for")
                .replyMarkup(getChooseTrackCategoryKeyboard())
                .build();
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void onChangeCampaignsPageButton(Update update, String arg) {
        EditMessageText editMessage = EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(update.getCallbackQuery().getMessage().getText())
                .replyMarkup(getCampaignsKeyboard(officialCampaigns, Integer.parseInt(arg)))
                .build();
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void onCampaignButton(Update update, String campaignIndex) {
        Campaign campaign = officialCampaigns.get(Integer.parseInt(campaignIndex));
        EditMessageText editMessage = EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(campaign.getName())
                .replyMarkup(getCampaignTracksKeyboard(campaign))
                .build();
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    //@Transactional
    private void onTrackButton(Update update, String mapIdString) {
        TelegramChat chat =
                chatRepository.findByChatId(update.getCallbackQuery().getMessage().getChatId());
        UUID mapId = UUID.fromString(mapIdString);
        List<MapRecord> records =
                customLeaderboardsService.getCustomLeaderboard(chat, mapId);

        MapInfo mapInfo = mapInfoRepository.findById(mapId).orElseThrow();
        String thumbnailUrl = mapInfo.getThumbnailUrl();
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .photo(new InputFile(thumbnailUrl))
                .caption(getRecordsMessage(records, mapInfo.getName()))
                .build();
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRecordsMessage(List<MapRecord> records, String mapName) {
        StringBuilder message = new StringBuilder();
        message.append(mapName).append("\n");
        if (records.isEmpty()) {
            message.append("No records yet");
        } else {
            for (int i = 1; i <= records.size(); i++) {
                MapRecord record = records.get(i - 1);
                message.append(i).append(". ");
                message.append(record.getAccount().getDisplayName()).append(" ");
                message.append(MessageUtils.millisecondsToTime(record.getTime()));
                message.append("\n");
            }
        }
        return message.toString();
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
