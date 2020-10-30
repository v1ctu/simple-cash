package com.github.v1ctu.command;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.dao.impl.UserDao;
import com.github.v1ctu.entity.UserEntity;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static com.github.v1ctu.util.Task.supplyAsync;

public class CashAddCommand {

    private final CashPlugin plugin;
    private final UserCache userCache;
    private final UserDao userDao;


    public CashAddCommand(CashPlugin plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
        this.userDao = plugin.getUserDao();
    }

    @Command(name = "addcash", permission = "cash.admin", usage = "addcash <target> <quantity>")
    public void execute(Context<CommandSender> context, OfflinePlayer target, double quantity) {
        CommandSender commandSender = context.getSender();
        UserEntity user = userCache.get(target.getUniqueId());

        if (quantity <= 0) {
            commandSender.sendMessage("§cVoce precisa setar um numero maior que zero.");
            return;
        }
        if (target.isOnline()) {
            user.setQuantity(user.getQuantity() + quantity);
            commandSender.sendMessage("§aVocê adicionou §f" + quantity + " §ade cash para o jogador: §f" + target.getName());
            return;
        }
        supplyAsync(() -> userDao.find(target.getUniqueId()))
                .whenComplete((userEntity, $) -> {
                    if (userEntity == null) {
                        commandSender.sendMessage("§cJogador nao encontrado.");
                    } else {
                        user.setQuantity(user.getQuantity() + quantity);
                        commandSender.sendMessage("§aVocê adicionou §f" + quantity + " §ade cash para o jogador: §f" + target.getName());
                    }
                });
    }
}
