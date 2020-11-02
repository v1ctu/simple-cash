package com.github.v1ctu.command;

import com.github.v1ctu.CashPlugin;
import com.github.v1ctu.dao.impl.UserDao;
import com.github.v1ctu.entity.UserEntity;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class CashTopCommand implements Listener {

    private final UserDao userDao;


    public CashTopCommand(CashPlugin plugin) {
        this.userDao = plugin.getUserDao();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Command(name = "topcash")
    public void execute(Context<Player> context) {
        Player sender = context.getSender();

        Inventory inventory = Bukkit.createInventory(null, 27, "§aTOP Cash do servidor");
        int slot = 11;

        for (UserEntity userEntity : userDao.find()) {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
            itemMeta.setOwner(userEntity.getName());
            itemMeta.setDisplayName("§7" + userEntity.getName());
            itemMeta.setLore(
                    Arrays.asList(
                            " ",
                            " §7Posição: §f" + (slot - 10),
                            " §7Cash: §f" + userEntity.getQuantity()
                    )
            );
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot++, itemStack);
        }
        sender.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("§aTOP Cash do servidor")) event.setCancelled(true);
    }
}


