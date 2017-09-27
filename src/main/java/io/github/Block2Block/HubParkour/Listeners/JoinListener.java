package io.github.Block2Block.HubParkour.Listeners;

import io.github.Block2Block.HubParkour.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Main.getMainConfig().getBoolean("Settings.Join-Message")) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Join-Message")));
        }
    }

}
