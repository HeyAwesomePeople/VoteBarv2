package me.HeyAwesomePeople.votebar;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.HeyAwesomePeople.votebar.configs.DataConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VoteBar extends JavaPlugin {
    public static VoteBar instance;
    public DataConfig data;

    public HashMap<UUID, SuperPlayer> players = new HashMap<UUID, SuperPlayer>();
    private File configf = new File(this.getDataFolder() + File.separator + "config.yml");
    public FileConfiguration config = getConfig();
    public FileConfiguration dataC = null;

    @Override
    public void onEnable() {
        instance = this;
        data = new DataConfig();
        dataC = data.getCustomConfig();
        getServer().getPluginManager().registerEvents(new Listener(), this);

        getCommand("votebar").setExecutor(new Commands());

        makeConfig();

        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
            PlaceholderAPI.registerPlaceholder(this, "votebar", new PlaceholderReplacer() {
                public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                    if (!players.containsKey(placeholderReplaceEvent.getPlayer().getUniqueId())) {
                        return "Error.";
                    }
                    return players.get(placeholderReplaceEvent.getPlayer().getUniqueId()).getVoteHashes();
                }
            });
            PlaceholderAPI.registerPlaceholder(this, "votebarpercent", new PlaceholderReplacer() {
                public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                    if (!players.containsKey(placeholderReplaceEvent.getPlayer().getUniqueId())) {
                        return "Error.";
                    }
                    return players.get(placeholderReplaceEvent.getPlayer().getUniqueId()).getVotePercentage();
                }
            });
            PlaceholderAPI.registerPlaceholder(this, "votebarminmax", new PlaceholderReplacer() {
                public String onPlaceholderReplace(PlaceholderReplaceEvent placeholderReplaceEvent) {
                    if (!players.containsKey(placeholderReplaceEvent.getPlayer().getUniqueId())) {
                        return "Error.";
                    }
                    return players.get(placeholderReplaceEvent.getPlayer().getUniqueId()).getVoteSlash();
                }
            });
        }
    }

    private void makeConfig() {
        if (!configf.exists()) {
            List<String> cmds = new ArrayList<String>();
            cmds.add("say %player 10 repeating gained");

            config.set("maxVotes", 10);
            config.set("depletionTime", 30);// minutes
            config.set("voteSymbol", "I");
            config.set("voteColor", "&3");
            config.set("nonVoteColor", "&4");
            config.set("run.10.repeatingCommand.interval", 100);// ticks
            config.set("run.10.repeatingCommand.chance", 50); // percentage
            config.set("run.10.repeatingCommand.commands", new ArrayList<String>(cmds));
            config.set("run.10.singleCommand.gainedPercent", new ArrayList<String>(cmds));
            config.set("run.10.singleCommand.lostPercent", new ArrayList<String>(cmds));
            dataC.set("data.0f91ede5-54ed-495c-aa8c-d87bf405d2bb.votes", new ArrayList<String>());
            saveConfig();
            data.saveCustomConfig();
        }
    }


}
