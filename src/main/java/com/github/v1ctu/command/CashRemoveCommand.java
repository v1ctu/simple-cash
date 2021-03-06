package com.github.v1ctu.command;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.dao.impl.UserDao;
import com.github.v1ctu.entity.UserEntity;
import com.github.v1ctu.util.Task;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import static com.github.v1ctu.util.Task.supplyAsync;

public class CashRemoveCommand {

    private final CashPlugin plugin;
    private final UserCache userCache;
    private final UserDao userDao;


    public CashRemoveCommand(CashPlugin plugin) {
        this.plugin = plugin;
        this.userCache = plugin.getUserCache();
        this.userDao = plugin.getUserDao();
    }

    @Command(name = "removecash", permission = "cash.admin", usage = "removecash <target> <quantity>")
    public void execute(Context<CommandSender> context, String target, double quantity) {
        CommandSender commandSender = context.getSender();
        UserEntity targetEntity = userCache.get(target);

        if (quantity <= 0) {
            commandSender.sendMessage("§cVoce precisa remover um numero maior que zero.");
            return;
        }

        if (targetEntity == null) {
            supplyAsync(() -> userDao.find(target))
                    .whenComplete((userEntity, $) -> {
                        if (userEntity == null) {
                            commandSender.sendMessage("§cJogador nao encontrado.");
                        } else {

                            if (userEntity.getQuantity() < 0) {
                                commandSender.sendMessage("§cEsse jogador possui uma quantia menor ou igual a 0, portanto nao foi possivel remover.");
                            }

                            if (userEntity.getQuantity() < quantity) {
                                commandSender.sendMessage("§cVoce nao pode remover uma quantidade maior do que o jogador possui.");
                                return;
                            }

                            userEntity.setQuantity(userEntity.getQuantity() - quantity);
                            userDao.replace(userEntity);
                            commandSender.sendMessage("§aVocê removeu §f" + quantity + " §ade cash do o jogador: §f" + target);
                        }
                    });
        } else {

            if (targetEntity.getQuantity() < 0) {
                commandSender.sendMessage("§cEsse jogador possui uma quantia menor ou igual a 0, portanto nao foi possivel remover.");
                return;
            }

            if (targetEntity.getQuantity() < quantity) {
                commandSender.sendMessage("§cVoce nao pode remover uma quantidade maior do que o jogador possui.");
                return;
            }

            targetEntity.setQuantity(targetEntity.getQuantity() - quantity);
            commandSender.sendMessage("§aVocê removeu §f" + quantity + " §ade cash do o jogador: §f" + target);
        }

    }
}
