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
    private static Map<Player, Long> parkourTimes = new HashMap<>();
    private static Map<Player, Integer> parkourChecks = new HashMap<>();
    private static Map<Player, Integer> checksVisited = new HashMap<>();

    private Location reset;
    private static HashMap<Integer, Location> checkpoints = new HashMap<>();
    private static Location start;
    private static Location end;
    private static Location restart;

    private static Material startType;
    private static Material endType;
    private static Material checkType;

    public static void setStartType(Material type) { startType = type; }
    public static void setEndType(Material type) { endType = type; }
    public static void setCheckType(Material type) { checkType = type; }

    public static Material getCheckType() {
        return checkType;
    }

    public static Material getEndType() {
        return endType;
    }

    public static Material getStartType() {
        return startType;
    }

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
    public static void setCheck(int number, Location location) {
        if (checkpoints.containsKey(number)) {
            checkpoints.remove(number);
        }
        checkpoints.put(number, location);
    }
    public static void setRestart(Location l) {restart = l;}

    public static Location getStart() {return start;}
    public static Location getEnd() {return end;}
    public static Location getCheck(int number) {return checkpoints.get(number);}
    public static Location getRestart() {return restart;}

    public static List<Location> getCheckLocations() {
        return checkLocations;
    }

    //TODO: Go over listener, add in support for MySQL

    @EventHandler
    public void onPressurePlate(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().getType().equals(e.getTo().getBlock().getType())) {
            return;
        }
        if (e.getPlayer().getLocation().getBlock().getType().equals(startType)) {
            if (e.getPlayer().getLocation().getBlock().getLocation().equals(start)) {
                if (parkourPlayers.contains(e.getPlayer())) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Restarted")));
                    parkourTimes.remove(e.getPlayer());
                    parkourTimes.put(e.getPlayer(), System.currentTimeMillis());
                    parkourChecks.remove(e.getPlayer());
                    parkourChecks.put(e.getPlayer(), 0);
                    checksVisited.remove(e.getPlayer());
                    checksVisited.put(e.getPlayer(), 0);
                    e.getPlayer().setFlying(false);
                } else {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Started")));
                    parkourPlayers.add(e.getPlayer());
                    parkourTimes.put(e.getPlayer(), System.currentTimeMillis());
                    parkourChecks.put(e.getPlayer(), 0);
                    checksVisited.put(e.getPlayer(), 0);
                    e.getPlayer().setFlying(false);
                }
            }
        } else if (e.getPlayer().getLocation().getBlock().getType().equals(checkType)) {
            Location l = e.getPlayer().getLocation().getBlock().getLocation();
            if (checkLocations.contains(l)) {
                if (parkourPlayers.contains(e.getPlayer())) {
                    int i = checkLocations.indexOf(l) + 1;
                    if (i > parkourChecks.get(e.getPlayer())) {
                        parkourChecks.remove(e.getPlayer());
                        parkourChecks.put(e.getPlayer(), i);
                        checksVisited.put(e.getPlayer(), checksVisited.get(e.getPlayer()) + 1);
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Parkour.Checkpoints.Reached")).replace("{checkpoint}", "" + i));
                        if (Main.getMainConfig().getBoolean("Settings.Rewards.Checkpoint-Reward.Enabled")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.getMainConfig().getString("Settings.Rewards.Checkpoint-Reward.Command").replace("{player-name}",e.getPlayer().getName()).replace("{player-uuid}",e.getPlayer().getUniqueId().toString()));
                        }
                    }

                } else {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Parkour.Checkpoints.Not-Started")));
                }
            }
        } else if (e.getPlayer().getLocation().getBlock().getType().equals(endType)) {
            if (e.getPlayer().getLocation().getBlock().getLocation().equals(end)) {
                if (parkourPlayers.contains(e.getPlayer())) {
                    if (Main.getMainConfig().getBoolean("Settings.Must-Complete-All-Checkpoints")) {
                        if (checksVisited.get(e.getPlayer()) != checkLocations.size()) {
                            if (PressurePlateInteractListener.removeParkourPlayersBoo(e.getPlayer())) {
                                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Failed.Not-Enough-Checkpoints")));
                                PressurePlateInteractListener.removeParkourPTimes(e.getPlayer());
                                PressurePlateInteractListener.removeParkourPlayers(e.getPlayer());
                                checksVisited.remove(e.getPlayer());
                                return;
                            }
                        }
                    }
                    long finishMili = (System.currentTimeMillis() - parkourTimes.get(e.getPlayer()));
                    float finishTime = finishMili/1000f;
                    if (Main.getMainConfig().getBoolean("Settings.Rewards.Finish-Reward.Enabled")) {
                        if (!Main.getLeaderboard().isSet("times." + e.getPlayer().getUniqueId() + ".time")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.getMainConfig().getString("Settings.Rewards.Finish-Reward.First-Time-Command").replace("{player-name}",e.getPlayer().getName()).replace("{player-uuid}",e.getPlayer().getUniqueId().toString()));
                        }
                        if (!Main.getMainConfig().getBoolean("Settings.Rewards.Finish-Reward.First-Time-Only")) {
                            if (Main.getLeaderboard().isSet("times." + e.getPlayer().getUniqueId() + ".time")) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.getMainConfig().getString("Settings.Rewards.Finish-Reward.After-Completed-Command").replace("{player-name}",e.getPlayer().getName()).replace("{player-uuid}",e.getPlayer().getUniqueId().toString()));
                            }
                        }
                    }
                    if (Main.getLeaderboard().isSet("times." + e.getPlayer().getUniqueId() + ".time")) {
                        if (Main.getLeaderboard().getLong("times." + e.getPlayer().getUniqueId() + ".time") > finishMili) {
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Beat-Previous-Personal-Best")).replace("{time}", "" + finishTime));
                            Main.getLeaderboard().set("times." + e.getPlayer().getUniqueId() + ".time", finishMili);
                            Main.saveYamls();
                        } else {
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.Not-Beat-Previous-Personal-Best")).replace("{time}", "" + finishTime));
                        }
                    } else {
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.End.First-Time")).replace("{time}", "" + finishTime));
                        Main.getLeaderboard().set("times." + e.getPlayer().getUniqueId() + ".time", (System.currentTimeMillis() - parkourTimes.get(e.getPlayer())));
                        Main.saveYamls();
                    }

                    if (Main.getLeaderboard().getLong("leaderboard.1.Time") > finishMili||e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.1.UUID").toString())) {
                        if (e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.1.UUID").toString())) {
                            if (Main.getLeaderboard().getLong("leaderboard.1.Time") > finishMili) {
                                Main.getLeaderboard().set("leaderboard.1.Time", finishMili);
                            }
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Still-First")));
                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        } else {
                            if (!Main.getLeaderboard().get("leaderboard.2.UUID").toString().equals(e.getPlayer().getUniqueId().toString())) {
                                Main.getLeaderboard().set("leaderboard.3.Time", Main.getLeaderboard().getLong("leaderboard.2.Time"));
                                Main.getLeaderboard().set("leaderboard.3.UUID", Main.getLeaderboard().get("leaderboard.2.UUID"));
                                Main.getLeaderboard().set("leaderboard.3.PlayerName", Main.getLeaderboard().get("leaderboard.2.PlayerName"));
                            }
                            Main.getLeaderboard().set("leaderboard.2.Time", Main.getLeaderboard().getLong("leaderboard.1.Time"));
                            Main.getLeaderboard().set("leaderboard.2.UUID", Main.getLeaderboard().get("leaderboard.1.UUID"));
                            Main.getLeaderboard().set("leaderboard.2.PlayerName", Main.getLeaderboard().get("leaderboard.1.PlayerName"));

                            Main.getLeaderboard().set("leaderboard.1.Time", finishMili);
                            Main.getLeaderboard().set("leaderboard.1.UUID", e.getPlayer().getUniqueId().toString());
                            Main.getLeaderboard().set("leaderboard.1.PlayerName", e.getPlayer().getName());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Now-First")));
                            Main.saveYamls();

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        }
                    } else if (Main.getLeaderboard().getDouble("leaderboard.2.Time") > finishMili||e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.2.UUID").toString())) {
                        if (e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.2.UUID").toString())) {
                            if (Main.getLeaderboard().getLong("leaderboard.2.Time") > finishMili) {
                                Main.getLeaderboard().set("leaderboard.2.Time", finishMili);
                            }
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Still-Second")));

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        } else {
                            Main.getLeaderboard().set("leaderboard.3.Time", Main.getLeaderboard().getLong("leaderboard.2.Time"));
                            Main.getLeaderboard().set("leaderboard.3.UUID", Main.getLeaderboard().get("leaderboard.2.UUID"));
                            Main.getLeaderboard().set("leaderboard.3.PlayerName", Main.getLeaderboard().get("leaderboard.2.PlayerName"));

                            Main.getLeaderboard().set("leaderboard.2.Time", finishMili);
                            Main.getLeaderboard().set("leaderboard.2.UUID", e.getPlayer().getUniqueId().toString());
                            Main.getLeaderboard().set("leaderboard.2.PlayerName", e.getPlayer().getName());
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Now-Second")));
                            Main.saveYamls();

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        }
                    }  else if (Main.getLeaderboard().getLong("leaderboard.3.Time") > finishMili||e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.3.UUID").toString())) {
                        if (e.getPlayer().getUniqueId().toString().equals(Main.getLeaderboard().get("leaderboard.3.UUID").toString())) {
                            if (Main.getLeaderboard().getLong("leaderboard.3.Time") > finishMili) {
                                Main.getLeaderboard().set("leaderboard.3.Time", finishMili);
                            }
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Parkour.Leaderboard.Still-Third")));

                            parkourChecks.remove(e.getPlayer());
                            parkourTimes.remove(e.getPlayer());
                            parkourPlayers.remove(e.getPlayer());
                            return;
                        } else {
                            Main.getLeaderboard().set("leaderboard.3.Time", finishMili);
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
