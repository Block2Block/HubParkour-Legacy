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
        if (e.getPlayer().hasPermission("hubparkour.admin")) {
            if (Main.getMainConfig().getBoolean("Settings.Version-Checker.Enabled")) {
                if (!Main.getMainConfig().getBoolean("Settings.Version-Checker.On-Join")) return;

                String version = Main.newVersionCheck();
                if (version != null) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&2Parkour>> &7HubParkour v&a" + version + " &7is out now! I highly recommend you download the new version! This message is only recieved by users with the permission hubparkour.admin"));
                } else {
                    Main.get().getLogger().info("Your HubParkour version is up to date!");
                }
            }
        }
    }

}
