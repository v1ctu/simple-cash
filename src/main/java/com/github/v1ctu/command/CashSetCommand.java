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

public class CashSetCommand {

    private final CashPlugin plugin;
    private final UserCache userCache;
    private final UserDao userDao;


    public CashSetCommand(CashPlugin plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
        this.userDao = plugin.getUserDao();
    }

    @Command(name = "setcash", permission = "cash.admin", usage = "setcash <target> <quantity>")
    public void execute(Context<CommandSender> context, String target, double quantity) {

        CommandSender commandSender = context.getSender();
        UserEntity user = userCache.get(target);

        if (quantity < 0) {
            commandSender.sendMessage("§cVoce precisa setar um numero maior ou igual a 0.");
            return;
        }

        if (user == null) {
            supplyAsync(() -> userDao.find(target))
                    .whenComplete((userEntity, $) -> {
                        if (userEntity == null) {
                            commandSender.sendMessage("§cJogador nao encontrado.");
                        } else {
                            userEntity.setQuantity(quantity);
                            userDao.replace(userEntity);
                            commandSender.sendMessage("§aVocê setou o cash do jogador: §f" + target + " §apara: §f" + userEntity.getQuantity());
                        }
                    });
        } else {
            user.setQuantity(quantity);
            commandSender.sendMessage("§aVocê setou o cash do jogador: §f" + target + " §apara: §f" + user.getQuantity());
        }


    }
}
