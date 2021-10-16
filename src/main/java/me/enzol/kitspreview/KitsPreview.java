package me.enzol.kitspreview;

import com.earth2me.essentials.Essentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.enzol.kitspreview.commands.KitEditPreviewCommand;
import me.enzol.kitspreview.commands.KitPreviewCommand;
import me.enzol.kitspreview.kitpreview.listeners.InventoryListener;
import me.enzol.kitspreview.kitpreview.listeners.KitEditListener;
import me.enzol.kitspreview.kitpreview.KitPreview;
import me.enzol.kitspreview.sign.SignListener;
import me.enzol.kitspreview.utils.Color;
import me.enzol.kitspreview.utils.adaters.ItemStackAdapter;
import me.enzol.kitspreview.utils.adaters.PotionEffectAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class KitsPreview extends JavaPlugin{

    @Getter private static KitsPreview instance;
    @Getter private static Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
        .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    @Override
    public void onEnable(){
        instance = this;

        this.saveDefaultConfig();

        checkConfig();

        if(Bukkit.getPluginManager().getPlugin("Essentials") == null){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Essentials not found");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerListeners();
        loadKits();
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
        Bukkit.getPluginManager().registerEvents(new KitEditListener(), this);
    }

    private void registerCommands(){
        KitPreviewCommand kitPreviewCommand = new KitPreviewCommand();
        KitEditPreviewCommand kitEditPreviewCommand = new KitEditPreviewCommand();

        this.getCommand("kitpreview").setExecutor(kitPreviewCommand);
        this.getCommand("kitpreview").setTabCompleter(kitPreviewCommand);

        this.getCommand("kiteditpreview").setExecutor(kitEditPreviewCommand);
        this.getCommand("kiteditpreview").setTabCompleter(kitEditPreviewCommand);
    }


    private void loadKits(){
        Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        ess.getKits().getKitKeys().forEach(kitName -> {
            KitPreview kitsPreview;
            try {
                kitsPreview = getGson().fromJson(new FileReader(this.getDataFolder()
                    + File.separator + kitName + ".json"), KitPreview.class);
            } catch (FileNotFoundException e) {
                kitsPreview = new KitPreview(kitName, 6);
            }

            KitPreview.getKITS().put(kitName.toLowerCase(), kitsPreview);
        });
    }

    private void checkConfig() {
        double configVersion = 2.0;
        String prefix = "&7[&bKitPreview&7] ";
        if (this.getConfig().get("version") == null || this.getConfig().getDouble("version") != configVersion) {
            Bukkit.getConsoleSender().sendMessage(Color.translate(prefix + "&cYou &7config.yml &cfile is outdated!"));
            Bukkit.getConsoleSender().sendMessage(Color.translate(prefix + "&7Starting &bconfig.yml &7update..."));
            this.getConfig().set("version", configVersion);
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
            this.saveDefaultConfig();
            Bukkit.getConsoleSender().sendMessage(Color.translate(prefix + "&7Update complete !"));
        }
        Bukkit.getConsoleSender().sendMessage(Color.translate(prefix + "&7You &bconfig/lang &7is updating!"));
    }
}
