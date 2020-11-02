package com.github.v1ctu.command;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.dao.impl.UserDao;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.github.v1ctu.util.Task.supplyAsync;

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
    public void execute(Context<Player> context, @Optional String target) {
        CommandSender commandSender = context.getSender();

        if (target == null) {
            commandSender.sendMessage("§aVocê possui: §f" + userCache.get(context.getSender().getName()).getQuantity() + " §ade cash.");
            return;
        }
        supplyAsync(() -> userDao.find(target))
                .whenComplete((userEntity, $) -> {
                    if (userEntity == null) {
                        commandSender.sendMessage("§cJogador nao encontrado.");
                    } else {
                        commandSender.sendMessage("§aO jogador: §f" + userEntity.getName() + " §apossui: §f" + userEntity.getQuantity() + " §ade cash.");
                    }
                });
    }
}
