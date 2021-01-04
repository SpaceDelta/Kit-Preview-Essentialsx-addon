package me.enzol.kitspreview.utils;

import me.enzol.kitspreview.KitsPreview;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        KitsPreview.getInstance().getServer().getScheduler().runTask(KitsPreview.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        KitsPreview.getInstance().getServer().getScheduler().runTaskTimer(KitsPreview.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(KitsPreview.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        KitsPreview.getInstance().getServer().getScheduler().runTaskLater(KitsPreview.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        KitsPreview.getInstance().getServer().getScheduler().runTaskAsynchronously(KitsPreview.getInstance(), runnable);
    }

}