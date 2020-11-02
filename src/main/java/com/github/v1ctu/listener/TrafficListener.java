package com.github.v1ctu.listener;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.impl.UserCache;
import com.github.v1ctu.dao.impl.UserDao;
import com.github.v1ctu.entity.UserEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static com.github.v1ctu.util.Task.runAsync;

public class TrafficListener implements Listener {

    private final CashPlugin plugin;
    private final UserDao userDao;
    private final UserCache userCache;

    public TrafficListener(CashPlugin plugin) {
        this.plugin = plugin;
        this.userDao = plugin.getUserDao();
        this.userCache = plugin.getUserCache();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        runAsync(() -> {
            Player player = event.getPlayer();
            String playerName = player.getName();

            UserEntity userEntity = userDao.find(playerName);
            if (userEntity == null) {
                UserEntity newUser = UserEntity.builder()
                        .uuid(player.getUniqueId())
                        .name(player.getName())
                        .quantity(0)
                        .build();
                userCache.put(playerName, newUser);
            } else {
                userCache.put(playerName, userEntity);
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        runAsync(() -> {
            String playerName = event.getPlayer().getName();
            UserEntity userEntity = userCache.get(playerName);

            userDao.replace(userEntity);
            userCache.remove(playerName);
        });
    }

}
