package ent.otego.saurus.integration.commands.leaderboard;

import ent.otego.saurus.integration.commands.Command;

public abstract class LeaderboardCommand<T> implements Command<T> {

    private final static String COMMAND_NAME = "leaderboard";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}
