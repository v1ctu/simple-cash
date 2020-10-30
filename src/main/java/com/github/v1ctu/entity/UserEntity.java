package com.github.v1ctu.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
public class UserEntity {

    private final UUID uuid;
    private final String name;
    @Setter
    private double quantity;

}
