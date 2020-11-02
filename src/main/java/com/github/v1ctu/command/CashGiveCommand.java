package com.github.v1ctu.command;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.dao.impl.UserDao;
import com.github.v1ctu.entity.UserEntity;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashGiveCommand {

    private final CashPlugin plugin;
    private final UserCache userCache;
    private final UserDao userDao;


    public CashGiveCommand(CashPlugin plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
        this.userDao = plugin.getUserDao();
    }

    @Command(name = "givecash", permission = "cash.admin", usage = "givecash <target> <quantity>")
    public void execute(Context<Player> context, Player target, double quantity) {
        CommandSender commandSender = context.getSender();
        UserEntity senderEntity = userCache.get(context.getSender().getName());
        UserEntity targetEntity = userCache.get(target.getName());



        if (quantity <= 0) {
            commandSender.sendMessage("§cVocê deve enviar uma quantia maior que zero.");
            return;
        }
        if (senderEntity.getQuantity() < quantity) {
            commandSender.sendMessage("§cVocê nao possui cash suficiente.");
        } else {
            targetEntity.setQuantity(targetEntity.getQuantity() + quantity);
            senderEntity.setQuantity(senderEntity.getQuantity() - quantity);
            target.sendMessage("§aVoce recebeu: §f" + quantity + "§a de cash do jogador: " + commandSender.getName());
            commandSender.sendMessage("§aVoce enivou: §f" + quantity + "§a de cash ao jogador: " + target.getName());
        }
    }
}
