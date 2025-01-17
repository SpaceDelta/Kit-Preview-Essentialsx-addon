package me.enzol.kitspreview.kitpreview.listeners;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class InventoryListener implements Listener{
	
    public static final List<UUID> OPEN_INVENTORIES = Lists.newArrayList();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        if(OPEN_INVENTORIES.contains(player.getUniqueId())) {
            if (event.getSlot() == 49) {
                player.chat("/kits");
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event){
        Player player = (Player)event.getWhoClicked();
        if(OPEN_INVENTORIES.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        if(OPEN_INVENTORIES.contains(player.getUniqueId())){
            OPEN_INVENTORIES.remove(player.getUniqueId());
            player.updateInventory();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        OPEN_INVENTORIES.remove(player.getUniqueId());
    }

}
