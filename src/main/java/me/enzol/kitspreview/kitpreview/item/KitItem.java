package me.enzol.kitspreview.kitpreview.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
@AllArgsConstructor
public class KitItem {

    private ItemStack item;
    private int slot;

}