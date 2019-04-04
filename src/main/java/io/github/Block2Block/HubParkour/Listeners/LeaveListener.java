package io.github.Block2Block.HubParkour.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        PressurePlateInteractListener.removeParkourPlayers(e.getPlayer());
        PressurePlateInteractListener.removeParkourPTimes(e.getPlayer());
        PressurePlateInteractListener.removeChecksVisited(e.getPlayer());
    }

}
