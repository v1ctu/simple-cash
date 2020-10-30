package com.github.v1ctu.cache.impl;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.cache.Cache;
import com.github.v1ctu.entity.UserEntity;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UserCache extends Cache<UUID, UserEntity> {

    private final CashPlugin plugin;

    @Override
    public void save() {
        for (UserEntity userEntity : values()) {
            plugin.getUserDao().replace(userEntity);
        }
    }
}
