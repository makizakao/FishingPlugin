package org.hark7.fishingPlugin.commands.group;

import org.hark7.fishingPlugin.commands.AddExpCommand;
import org.hark7.fishingPlugin.database.FishExpManager;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class FishCommandGroup extends SimpleCommandGroup {
    private final FishExpManager manager;
    public FishCommandGroup(FishExpManager manager) {
        super("fish");
        this.manager = manager;
    }
    @Override
    protected String getCredits() {
        return "";
    }

    @Override
    protected void registerSubcommands() {
        registerSubcommand(new AddExpCommand(manager));
    }
}
