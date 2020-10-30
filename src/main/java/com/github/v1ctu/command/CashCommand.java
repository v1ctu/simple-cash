package com.github.v1ctu.command;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.dao.impl.UserDao;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand {

    private final CashPlugin plugin;
    private final UserCache userCache;
    private final UserDao userDao;


    public CashCommand(CashPlugin plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
        this.userDao = plugin.getUserDao();
    }


    @Command(name = "cash")
    public void execute(Context<Player> context, @Optional OfflinePlayer target) {
        CommandSender commandSender = context.getSender();

        if (target == null) {
            commandSender.sendMessage("§aVocê possui: §f" + userCache.get(context.getSender().getUniqueId()).getQuantity() + " §ade cash.");
            return;
        }
        if (target.isOnline()) {
            commandSender.sendMessage("§a" + target.getName() + " possui: §f" + userCache.get(target.getUniqueId()).getQuantity() + " §ade cash.");
        } else {
            commandSender.sendMessage("§cJogador nao encontrado.");
        }
    }
}
