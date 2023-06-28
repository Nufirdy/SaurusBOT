package ent.otego.saurus.integration.commands.addPlayer;

import ent.otego.saurus.core.model.PlayerAccount;

public record AddPlayerResult(AddPlayerResultEnum result,
                              PlayerAccount playerAccount) {

}
