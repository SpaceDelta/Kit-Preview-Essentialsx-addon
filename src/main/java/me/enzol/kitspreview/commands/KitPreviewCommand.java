package me.enzol.kitspreview.commands;

import com.earth2me.essentials.Essentials;
import me.enzol.kitspreview.KitsPreview;
import me.enzol.kitspreview.kitpreview.listeners.InventoryListener;
import me.enzol.kitspreview.kitpreview.KitPreview;
import me.enzol.kitspreview.utils.Color;
import me.enzol.kitspreview.utils.EssentialsUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KitPreviewCommand implements CommandExecutor, TabExecutor{

    private static KitsPreview plugin = KitsPreview.getInstance();
    private static Configuration config = plugin.getConfig();
    private final ItemStack backButton;

    public KitPreviewCommand() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "EXIT");
        meta.setLore(Collections.singletonList(ChatColor.WHITE + "Return to the kits menu."));
        item.setItemMeta(meta);

        this.backButton = item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players");
            return false;
        }

        Player player = (Player) sender;

        if(args.length < 1){
            player.sendMessage(Color.translate( "&bUse&7: "));
            player.sendMessage(Color.translate("&b/kitpreview <kit name>"));
            return false;
        }

        if(player.isSleeping()) return false;

        String kitName = args[0];

        KitPreview kitPreview = KitPreview.getByName(kitName);

        if(kitPreview == null){
            player.sendMessage(ChatColor.RED + "Kit not found");
            return false;
        }

        Inventory inventory = Bukkit.createInventory(null,
            9 * kitPreview.getRows(),
            Color.translate(config.getString("gui.title")
                    .replace("{kit}", StringUtils.capitalize(kitName)))); // SpaceDelta - capitalize

        if(kitPreview.getItems().isEmpty()){
            EssentialsUtils.getItems(player, kitName).forEach(inventory::addItem);
        }else {
            kitPreview.getItems().forEach(kitItem -> inventory.setItem(kitItem.getSlot(), kitItem.getItem()));
        }

        inventory.setItem(49, backButton); // SpaceDelta - back button

        player.openInventory(inventory);

        InventoryListener.OPEN_INVENTORIES.add(player.getUniqueId());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args){
        Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (args.length == 1) {
            return new ArrayList<>(ess.getKits().getConfig().getKeys()).stream()
                .filter(s1 -> s1.toLowerCase().startsWith(args[0]))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
