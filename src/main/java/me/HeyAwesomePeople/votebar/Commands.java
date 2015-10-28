package me.HeyAwesomePeople.votebar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class Commands implements CommandExecutor {

    private static VoteBar plugin = VoteBar.instance;

    public boolean onCommand(final CommandSender sender, Command cmd,
                             String commandLabel, final String[] args) {
        if (!sender.hasPermission("votebar.admin")) {
            sender.sendMessage(ChatColor.RED + "No permissions.");
            return false;
        }
        if (commandLabel.equalsIgnoreCase("votebar")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "[VoteBar]");
                sender.sendMessage(ChatColor.AQUA + "/votebar add <player> <amount>");
                sender.sendMessage(ChatColor.AQUA + "/votebar remove <player> <amount>");
                sender.sendMessage(ChatColor.AQUA + "/votebar set <player> <amount>");
                sender.sendMessage(ChatColor.AQUA + "/votebar reload");
            } else {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        plugin.config = plugin.getConfig();
                        sender.sendMessage(ChatColor.GREEN + "VoteBar config reloaded!");
                    }
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Not enough arguments.");
                    return false;
                }

                OfflinePlayer p = null;
                p = (OfflinePlayer) Bukkit.getPlayer(args[1]);
                if (p == null) {
                    for (String s : plugin.config.getConfigurationSection("data").getKeys(false)) {
                        if (plugin.config.contains("data." + s + ".username")) {
                            p = Bukkit.getOfflinePlayer(UUID.fromString(s));
                        }
                    }
                }
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found! (Has never played before!)");
                    return false;
                }

                SuperPlayer sp = null;
                if (plugin.players.containsKey(p.getUniqueId())) {
                    sp = plugin.players.get(p.getUniqueId());
                } else {
                    sp = new SuperPlayer(p);
                }

                if (args[0].equalsIgnoreCase("add")) {
                    if (!Methods.isInteger(args[2])) {
                        sender.sendMessage(ChatColor.RED + "Amount of votes to add must be a number!");
                        return false;
                    }
                    sp.addVotes(Integer.parseInt(args[2]));
                    sender.sendMessage(ChatColor.AQUA + "[VoteBar] Added " + args[2] + " votes.");
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!Methods.isInteger(args[2])) {
                        sender.sendMessage(ChatColor.RED + "Amount of votes to add must be a number!");
                        return false;
                    }
                    sp.removeVotes(Integer.parseInt(args[2]));
                    sender.sendMessage(ChatColor.AQUA + "[VoteBar] Removed " + args[2] + " votes.");
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (!Methods.isInteger(args[2])) {
                        sender.sendMessage(ChatColor.RED + "Amount of votes to add must be a number!");
                        return false;
                    }
                    sp.setVotes(Integer.parseInt(args[2]));
                    sender.sendMessage(ChatColor.AQUA + "[VoteBar] Set " + args[2] + " votes.");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Unknown subcommand!");
                }
            }
        }
        return false;
    }
}
