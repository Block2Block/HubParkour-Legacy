package io.github.Block2Block.HubParkour.Commands;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import io.github.Block2Block.HubParkour.Listeners.PressurePlateInteractListener;
import io.github.Block2Block.HubParkour.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static io.github.Block2Block.HubParkour.Listeners.PressurePlateInteractListener.*;

public class CommandParkour implements CommandExecutor {

    private Main m = Main.get();

    //TODO: Go over commands with aim of adding in MySQL support.

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Arguments")));
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reset":
                    if (getParkourPlayers().contains((Player) sender)) {
                        Location l = getRestart();
                        ((Player) sender).teleport(l);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Reset.Successful")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Reset.Not-Started-Parkour")));
                    }
                    break;
                case "checkpoint":
                    if (getParkourPlayers().contains((Player) sender)) {
                        if (getParkourChecks().get((Player) sender).equals(0)) {
                            Location l = getRestart();
                            ((Player) sender).teleport(l);
                        } else {
                            Location l = getCheck(getParkourChecks().get((Player) sender));
                            ((Player) sender).teleport(l);
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Checkpoint.Successful")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Checkpoint.Not-Started-Parkour")));
                    }
                    break;
                case "top3":
                case "leaderboard":
                    if (Main.dbEnabled) {
                        HashMap<Integer, List<String>> leaderboard = Main.db.getLeaderboard();
                        List<String> pos1 = leaderboard.get(1);
                        List<String> pos2 = leaderboard.get(2);
                        List<String> pos3 = leaderboard.get(3);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Top3").replace("{player1-name}",pos1.get(0)).replace("{player1-time}", "" + Float.parseFloat(pos1.get(1))/1000f).replace("{player2-name}",pos2.get(0)).replace("{player2-time}", "" + Float.parseFloat(pos2.get(1))/1000f).replace("{player3-name}",pos3.get(0)).replace("{player3-time}", "" + Float.parseFloat(pos3.get(1))/1000f)));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Top3").replace("{player1-name}", Main.getLeaderboard().getString("leaderboard.1.PlayerName")).replace("{player1-time}", "" + Float.parseFloat(Main.getLeaderboard().getString("leaderboard.1.Time")) / 1000f).replace("{player2-name}", Main.getLeaderboard().getString("leaderboard.2.PlayerName")).replace("{player2-time}", "" + Float.parseFloat(Main.getLeaderboard().getString("leaderboard.2.Time")) / 1000f).replace("{player3-name}", Main.getLeaderboard().getString("leaderboard.3.PlayerName")).replace("{player3-time}", "" + Float.parseFloat(Main.getLeaderboard().getString("leaderboard.3.Time")) / 1000f)));
                    }
                    break;
                case "setstart":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        if (((Player) sender).getLocation().getBlock().getType().equals(PressurePlateInteractListener.getStartType())) {
                            if (Main.dbEnabled) {
                                if (PressurePlateInteractListener.getStart().equals(null)) {
                                    Main.db.addLocation(((Player) sender).getLocation().getBlock().getLocation(), 0, -1);
                                } else {
                                    Main.db.setLocation(((Player) sender).getLocation().getBlock().getLocation(), 0, -1);
                                }
                            } else {
                                Main.addStorage("spawn.location", ((Player) sender).getLocation().getBlock().getLocation());
                            }

                            Location l = ((Player) sender).getLocation().getBlock().getLocation();
                            if (Main.isHologramsActive()) {
                                if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                    l.setY(l.getY() + 2);
                                    l.setX(l.getX() + 0.5);
                                    l.setZ(l.getZ() + 0.5);
                                    Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Holograms.Start")));
                                    Main.getHolograms().add(hologram);
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    l.setY(l.getY() - 2);
                                }
                            }
                            setStart(l);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetStart.Successful")));

                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetStart.Invalid-Location")));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.No-Permission")));
                    }
                    break;
                case "setend":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        if (((Player) sender).getLocation().getBlock().getType().equals(PressurePlateInteractListener.getEndType())) {
                            if (Main.dbEnabled) {
                                if (PressurePlateInteractListener.getEnd().equals(null)) {
                                    Main.db.addLocation(((Player) sender).getLocation().getBlock().getLocation(), 1, -1);
                                } else {
                                    Main.db.setLocation(((Player) sender).getLocation().getBlock().getLocation(), 1, -1);
                                }
                            } else {
                                Main.addStorage("end.location", ((Player) sender).getLocation().getBlock().getLocation());
                            }
                            Location l = ((Player) sender).getLocation().getBlock().getLocation();
                            if (Main.isHologramsActive()) {
                                if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                    l.setY(l.getY() + 2);
                                    l.setX(l.getX() + 0.5);
                                    l.setZ(l.getZ() + 0.5);
                                    Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Holograms.End")));
                                    Main.getHolograms().add(hologram);
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    l.setY(l.getY() - 2);
                                }
                            }
                            setEnd(l);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetEnd.Successful")));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetEnd.Invalid-Location")));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Permission")));
                    }
                    break;
                case "setrestart":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        Location l = ((Player) sender).getLocation();
                        if (Main.dbEnabled) {
                            if (PressurePlateInteractListener.getRestart().equals(null)) {
                                Main.db.addLocation(l, 2, -1);
                            } else {
                                Main.db.setLocation(l, 2, -1);
                            }
                        } else {
                            Main.addStorage("reset.location", l);
                        }
                        PressurePlateInteractListener.setRestart(l);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetRestart.Successful")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Permission")));
                    }
                    break;
                case "setcheck":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        if (((Player) sender).getLocation().getBlock().getType().equals(Material.GOLD_PLATE)) {
                            try {
                                int i = Integer.parseInt(args[1]);
                            } catch(NumberFormatException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Number-Format-Error")));
                                return true;
                            } catch(ArrayIndexOutOfBoundsException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Invalid-Arguments")));
                                return true;
                            }
                            if (Main.dbEnabled) {
                                if (!PressurePlateInteractListener.getCheck(Integer.parseInt(args[1])).equals(null)) {
                                    Main.db.addLocation(((Player) sender).getLocation().getBlock().getLocation(), 3, Integer.parseInt(args[1]));
                                } else {
                                    Main.db.setLocation(((Player) sender).getLocation().getBlock().getLocation(), 3, Integer.parseInt(args[1]));
                                }
                            } else {
                                Main.addStorage(args[1] + ".location", ((Player) sender).getLocation().getBlock().getLocation());
                            }
                            Location l = ((Player) sender).getLocation().getBlock().getLocation();
                            if (Main.isHologramsActive()) {
                                if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                    l.setY(l.getY() + 2);
                                    l.setX(l.getX() + 0.5);
                                    l.setZ(l.getZ() + 0.5);
                                    Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Holograms.Checkpoint")).replace("{checkpoint}", args[1]));
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    l.setY(l.getY() - 2);
                                    Main.getHolograms().add(hologram);
                                }
                            }
                            getCheckLocations().add(l);
                            setCheck(Integer.parseInt(args[1]), l);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Successful").replace("{checkpoint}",args[1])));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Invalid-Location")));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Permission")));
                    }
                    break;
                case "reload":
                    if (sender.hasPermission("hubparkour.admin")) {
                        try {
                            Main.loadYamls();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.Reload.Successful")));
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.Reload.Failed")));
                        }
                        break;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Permission")));
                        break;
                    }

                case "leave":
                    if (getParkourPlayers().contains((Player) sender)) {
                        removeParkourPlayers((Player) sender);
                        removeParkourPTimes((Player) sender);
                        removeChecksVisited((Player) sender);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Leave.Left")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Leave.NotInParkour")));
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Unknown-Subcommand")));
                    break;
            }
        }
        return true;
    }
}
