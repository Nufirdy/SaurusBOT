package ent.otego.saurus.integration.commands.addPlayer;

import static ent.otego.saurus.integration.commands.addPlayer.AddPlayerResultEnum.*;

import ent.otego.saurus.core.CustomLeaderboardsService;
import ent.otego.saurus.core.exceptions.PlayerDoesNotExistException;
import ent.otego.saurus.core.model.PlayerAccount;
import ent.otego.saurus.integration.*;
import ent.otego.saurus.integration.commands.Command;
import ent.otego.saurus.integration.model.Subscriber;
import java.util.UUID;

public abstract class AddPlayerCommand<T> implements Command<T> {

    private final static String COMMAND_NAME = "addPlayer";
    private final CustomLeaderboardsService customLeaderboardsService;

    public AddPlayerCommand(
            CustomLeaderboardsService customLeaderboardsService
    ) {
        this.customLeaderboardsService = customLeaderboardsService;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void executeCommand(ChatMessage<T> message) {
        Subscriber subscriber = getSubscriber(message.getMessengerData());
        try {
            UUID accountId = getAccountId(message.getMessengerData());
            AddPlayerResult addPlayerResult =
                    customLeaderboardsService.addPlayerToLeaderboard(subscriber, accountId);
            if (addPlayerResult.result() == SUCCESS) {
                playerAddedToLeaderboard(message.getMessengerData(),
                        addPlayerResult.playerAccount());
            } else if (addPlayerResult.result() == ALREADY_EXISTS) {
                leaderboardAlreadyHasPlayer(message.getMessengerData(),
                        addPlayerResult.playerAccount());
            }
        } catch (PlayerDoesNotExistException | IllegalArgumentException e) {
            playerDoesNotExist(message.getMessengerData());
        }
    }

    protected abstract Subscriber getSubscriber(T messengerData);

    protected abstract UUID getAccountId(T messengerData);

    protected abstract void playerAddedToLeaderboard(
            T messengerData,
            PlayerAccount playerAccount
    );

    protected abstract void leaderboardAlreadyHasPlayer(
            T messengerData,
            PlayerAccount playerAccount
    );

    protected abstract void playerDoesNotExist(T messengerData);
}
