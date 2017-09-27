package io.github.Block2Block.HubParkour.Listeners;

import io.github.Block2Block.HubParkour.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerToggleFlyListener implements Listener {

    private Main m = Main.get();

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        if (e.isFlying()) {
            if (PressurePlateInteractListener.removeParkourPlayersBoo(e.getPlayer())) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',m.getMainConfig().getString("Messages.Parkour.Failed.Fly")));
                PressurePlateInteractListener.removeParkourPTimes(e.getPlayer());
                PressurePlateInteractListener.removeChecksVisited(e.getPlayer());
            }
        }
    }

}
