package io.github.Block2Block.HubParkour.Listeners;

import io.github.Block2Block.HubParkour.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PressurePlateInteractListener implements Listener {

    private static List<Location> checkLocations = new ArrayList<>();

    private Main m = Main.get();

    private static List<Player> parkourPlayers = new ArrayList<>();
    private static Map<Player, Double> parkourTimes = new HashMap<>();
    private static Map<Player, Integer> parkourChecks = new HashMap<>();
    private static Map<Player, Integer> checksVisited = new HashMap<>();

    private Location reset;
    private List<Location> checkpoints = new ArrayList<>();
    private static Location start;
    private static Location end;

    /*
    Gold = Checkpoint, Iron = Finish, Wooden = Start
     */

    public static Map<Player, Integer> getParkourChecks() { return parkourChecks; }

    public static List<Player> getParkourPlayers() { return parkourPlayers; }

    public static void removeChecksVisited(Player p) {
        checksVisited.remove(p);
    }

    public static void removeParkourPlayers(Player player) {
        if (parkourPlayers.contains(player)) {
            parkourPlayers.remove(player);
        }
    }

    public static boolean removeParkourPlayersBoo(Player player) {
        if (parkourPlayers.contains(player)) {
            parkourPlayers.remove(player);
            return true;
        }
        return false;
    }

    public static void removeParkourPTimes(Player player) {
        if (parkourTimes.containsKey(player)) {
            parkourTimes.remove(player);
        }
    }
    public static void setStart(Location l) {
        start = l;
    }
    public static void setEnd(Location l) {
        end = l;
    }

    public static List<Location> getCheckLocations() {
        return checkLocations;
    }


    @EventHandler
    public void timerEvent(TimerEvent e) {
        for (Player p : parkourPlayers) {
            double d = parkourTimes.get(p);
            parkourTimes.remove(p);
            parkourTimes.put(p, d + 1);
        }
    }

    @EventHandler
    public void onPressurePlate(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().getType().equals(e.getTo().getBlock().getType())) {
            return;
        }
        if (e.getPlayer().getLocation().getBlock().getType().equals(Material.WOOD_PLATE)) {
            if (e.getPlayer().getLocation().getBlock().getLocation().equals(start)) {
                if (parkourPlayers.contains(e.getPlayer())) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Restarted")));
                    parkourTimes.remove(e.getPlayer());
                    parkourTimes.put(e.getPlayer(), 0.00);
                    parkourChecks.remove(e.getPlayer());
                    parkourChecks.put(e.getPlayer(), 0);
                    checksVisited.remove(e.getPlayer());
                    checksVisited.put(e.getPlayer(), 0);
                } else {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Started")));
                    parkourPlayers.add(e.getPlayer());
                    parkourTimes.put(e.getPlayer(), 0.00);
                    parkourChecks.put(e.getPlayer(), 0);
                    checksVisited.put(e.getPlayer(), 0);
                }
            }
        } else if (e.getPlayer().getLocation().getBlock().getType().equals(Material.GOLD_PLATE)) {
            if (parkourPlayers.contains(e.getPlayer())) {
                Location l = e.getPlayer().getLocation().getBlock().getLocation();
                if (checkLocations.contains(l)) {
                    int i = checkLocations.indexOf(l) + 1;
                    if (i > parkourChecks.get(e.getPlayer())) {
                        parkourChecks.remove(e.getPlayer());
                        parkourChecks.put(e.getPlayer(), i);
                        checksVisited.put(e.getPlayer(), checksVisited.get(e.getPlayer())+1);
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Checkpoints.Reached")).replace("{checkpoint}","" + i));
                        if (Main.getMainConfig().getBoolean("Settings.Rewards.Checkpoint-Reward.Enabled")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.getMainConfig().getString("Settings.Rewards.Checkpoint-Reward.Command"));
                        }
                    }
                }
            } else {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Checkpoints.Not-Started")));
            }
        } else if (e.getPlayer().getLocation().getBlock().getType().equals(Material.IRON_PLATE)) {
            if (e.getPlayer().getLocation().getBlock().getLocation().equals(end)) {
                if (parkourPlayers.contains(e.getPlayer())) {
                    if (Main.getMainConfig().getBoolean("Settings.Must-Complete-All-Checkpoints")) {
                        if (checksVisited.get(e.getPlayer()) != checkLocations.size()) {
                            if (PressurePlateInteractListener.removeParkourPlayersBoo(e.getPlayer())) {
                                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Failed.Not-Enough-Checkpoints")));
                                PressurePlateInteractListener.removeParkourPTimes(e.getPlayer());
                                checksVisited.remove(e.getPlayer());
                            }
                        }
                    }
                    if (Main.getMainConfig().getBoolean("Settings.Rewards.Finish-Reward.Enabled")) {
                        if (!Main.getLeaderboard().isSet("times." + e.getPlayer().getUniqueId() + ".time")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.getMainConfig().getString("Settings.Rewards.Finish-Reward.First-Time-Command"));
                        }
                        if (!Main.getMainConfig().getBoolean("Settings.Rewards.Finish-Reward.First-Time-Only")) {
                            if (Main.getLeaderboard().isSet("times." + e.getPlayer().getUniqueId() + ".time")) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.getMainConfig().getString("Settings.Rewards.Finish-Reward.After-Completed-Command"));
                            }
                        }
                    }
                    if (Main.getLeaderboard().isSet("times." + e.getPlayer().getUniqueId() + ".time")) {
                        if (Main.getLeaderboard().getDouble("times." + e.getPlayer().getUniqueId() + ".time") > parkourTimes.get(e.getPlayer())/20) {
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Beat-Previous-Personal-Best")).replace("{time}", "" + parkourTimes.get(e.getPlayer())/20));
                            Main.getLeaderboard().set("times." + e.getPlayer().getUniqueId() + ".time", parkourTimes.get(e.getPlayer())/20);
                            Main.saveYamls();
                        } else {
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Not-Beat-Previous-Personal-Best")).replace("{time}", "" + parkourTimes.get(e.getPlayer())/20));
                        }
                    } else {
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.First-Time")).replace("{time}", "" + parkourTimes.get(e.getPlayer())/20));
                        Main.getLeaderboard().set("times." + e.getPlayer().getUniqueId() + ".time", parkourTimes.get(e.getPlayer())/20);
                        Main.saveYamls();
                    }

                    if (Main.getLeaderboard().getDouble("leaderboard.1.Time") > parkourTimes.get(e.getPlayer())/20||e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.1.UUID").toString())) {
                        if (e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.1.UUID").toString())) {
                            if (Main.getLeaderboard().getDouble("leaderboard.1.Time") > parkourTimes.get(e.getPlayer())/20) {
                                Main.getLeaderboard().set("leaderboard.1.Time", parkourTimes.get(e.getPlayer()) / 20);
                            }
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Still-First")));
                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        } else {
                            if (!Main.getLeaderboard().get("leaderboard.2.UUID").toString().equals(e.getPlayer().getUniqueId().toString())) {
                                Main.getLeaderboard().set("leaderboard.3.Time", Main.getLeaderboard().getDouble("leaderboard.2.Time"));
                                Main.getLeaderboard().set("leaderboard.3.UUID", Main.getLeaderboard().get("leaderboard.2.UUID"));
                                Main.getLeaderboard().set("leaderboard.3.PlayerName", Main.getLeaderboard().get("leaderboard.2.PlayerName"));
                            }
                            Main.getLeaderboard().set("leaderboard.2.Time", Main.getLeaderboard().getDouble("leaderboard.1.Time"));
                            Main.getLeaderboard().set("leaderboard.2.UUID", Main.getLeaderboard().get("leaderboard.1.UUID"));
                            Main.getLeaderboard().set("leaderboard.2.PlayerName", Main.getLeaderboard().get("leaderboard.1.PlayerName"));

                            Main.getLeaderboard().set("leaderboard.1.Time", parkourTimes.get(e.getPlayer())/20);
                            Main.getLeaderboard().set("leaderboard.1.UUID", e.getPlayer().getUniqueId().toString());
                            Main.getLeaderboard().set("leaderboard.1.PlayerName", e.getPlayer().getName());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Now-First")));
                            Main.saveYamls();

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        }
                    } else if (Main.getLeaderboard().getDouble("leaderboard.2.Time") > parkourTimes.get(e.getPlayer())/20||e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.2.UUID").toString())) {
                        if (e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.2.UUID").toString())) {
                            if (Main.getLeaderboard().getDouble("leaderboard.2.Time") > parkourTimes.get(e.getPlayer())/20) {
                                Main.getLeaderboard().set("leaderboard.2.Time", parkourTimes.get(e.getPlayer()) / 20);
                            }
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Still-Second")));

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        } else {
                            Main.getLeaderboard().set("leaderboard.3.Time", Main.getLeaderboard().getDouble("leaderboard.2.Time"));
                            Main.getLeaderboard().set("leaderboard.3.UUID", Main.getLeaderboard().get("leaderboard.2.UUID"));
                            Main.getLeaderboard().set("leaderboard.3.PlayerName", Main.getLeaderboard().get("leaderboard.2.PlayerName"));

                            Main.getLeaderboard().set("leaderboard.2.Time", parkourTimes.get(e.getPlayer())/20);
                            Main.getLeaderboard().set("leaderboard.2.UUID", e.getPlayer().getUniqueId().toString());
                            Main.getLeaderboard().set("leaderboard.2.PlayerName", e.getPlayer().getName());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Now-Second")));
                            Main.saveYamls();

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        }
                    }  else if (Main.getLeaderboard().getDouble("leaderboard.3.Time") > parkourTimes.get(e.getPlayer())/20||e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.3.UUID").toString())) {
                        if (e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.3.UUID").toString())) {
                            if (Main.getLeaderboard().getDouble("leaderboard.3.Time") > parkourTimes.get(e.getPlayer())/20) {
                                Main.getLeaderboard().set("leaderboard.3.Time", parkourTimes.get(e.getPlayer()) / 20);
                            }
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Still-Third")));

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        } else {
                            Main.getLeaderboard().set("leaderboard.3.Time", parkourTimes.get(e.getPlayer())/20);
                            Main.getLeaderboard().set("leaderboard.3.UUID", e.getPlayer().getUniqueId().toString());
                            Main.getLeaderboard().set("leaderboard.3.PlayerName", e.getPlayer().getName());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Now-Third")));
                            Main.saveYamls();

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        }
                    }
                    parkourChecks.remove(e.getPlayer());
                    parkourTimes.remove(e.getPlayer());
                    parkourPlayers.remove(e.getPlayer());
                    return;
                } else {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Not-Started")));
                }
            }
        }
    }

}
