package com.github.v1ctu;

import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.command.*;
import com.github.v1ctu.dao.impl.UserDao;
import com.github.v1ctu.listener.TrafficListener;
import com.github.v1ctu.storage.MySQL;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CashPlugin extends JavaPlugin {

    private MySQL mySql;
    private UserCache userCache;
    private UserDao userDao;

    @Override
    public void onEnable() {
        initVariables();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        userCache.save();
    }

    private void initVariables() {
        mySql = new MySQL("", "", "", "", 3306);
        mySql.openConnection();
        userCache = new UserCache(this);
        userDao = new UserDao(this);
    }

    private void registerCommands() {
        BukkitFrame bukkitFrame = new BukkitFrame(this);

        bukkitFrame.getMessageHolder().setMessage(MessageType.INCORRECT_TARGET, "§cEsse comando não foi feito para você.");
        bukkitFrame.getMessageHolder().setMessage(MessageType.INCORRECT_USAGE, "§cPor favor use §f/{usage}§c.");
        bukkitFrame.getMessageHolder().setMessage(MessageType.NO_PERMISSION, "§cVocê não tem permissão para executar esse comando.");

        bukkitFrame.registerCommands(
                new CashCommand(this),
                new CashSetCommand(this),
                new CashAddCommand(this),
                new CashRemoveCommand(this),
                new CashGiveCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new TrafficListener(this), this);
    }

}
