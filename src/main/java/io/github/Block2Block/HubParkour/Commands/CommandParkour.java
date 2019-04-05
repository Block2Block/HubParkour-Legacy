package io.github.Block2Block.HubParkour.Commands;

import com.apple.eawt.AppEvent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.github.Block2Block.HubParkour.Listeners.PressurePlateInteractListener.*;

public class CommandParkour implements CommandExecutor {

    private Main m = Main.get();


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
                            l.setX(l.getX() + 0.5);
                            l.setZ(l.getZ() + 0.5);
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
                        List<String> pos1 = new ArrayList<>();
                        pos1.add("N/A");
                        pos1.add("0");
                        List<String> pos2 = new ArrayList<>();
                        pos2.add("N/A");
                        pos2.add("0");
                        List<String> pos3 = new ArrayList<>();
                        pos3.add("N/A");
                        pos3.add("0");
                        switch (leaderboard.size()) {
                            case 3:
                                pos3 = leaderboard.get(3);
                            case 2:
                                pos2 = leaderboard.get(2);
                            case 1:
                                pos1 = leaderboard.get(1);
                                break;
                        }

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Top3").replace("{player1-name}",pos1.get(0)).replace("{player1-time}", "" + Float.parseFloat(pos1.get(1))/1000f).replace("{player2-name}",pos2.get(0)).replace("{player2-time}", "" + Float.parseFloat(pos2.get(1))/1000f).replace("{player3-name}",pos3.get(0)).replace("{player3-time}", "" + Float.parseFloat(pos3.get(1))/1000f)));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Top3").replace("{player1-name}", Main.getLeaderboard().getString("leaderboard.1.PlayerName")).replace("{player1-time}", "" + Float.parseFloat(Main.getLeaderboard().getString("leaderboard.1.Time")) / 1000f).replace("{player2-name}", Main.getLeaderboard().getString("leaderboard.2.PlayerName")).replace("{player2-time}", "" + Float.parseFloat(Main.getLeaderboard().getString("leaderboard.2.Time")) / 1000f).replace("{player3-name}", Main.getLeaderboard().getString("leaderboard.3.PlayerName")).replace("{player3-time}", "" + Float.parseFloat(Main.getLeaderboard().getString("leaderboard.3.Time")) / 1000f)));
                    }
                    break;
                case "setstart":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        if (((Player) sender).getLocation().getBlock().getType().equals(PressurePlateInteractListener.getStartType())) {
                            boolean generateHolograms = false;
                            Location l = ((Player) sender).getLocation().getBlock().getLocation();
                            if (PressurePlateInteractListener.getEnd() != null && PressurePlateInteractListener.getRestart() != null) {
                                if (getStart() != null) {
                                    Hologram h = Main.getHolograms().get(0);
                                    h.delete();
                                    Main.getHolograms().remove(0);
                                    l.setX(l.getX() + 0.5);
                                    l.setZ(l.getZ() + 0.5);
                                    l.setY(l.getY() + 2);
                                    Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Holograms.Start")));
                                    Main.getHolograms().add(0, hologram);
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    l.setY(l.getY() - 2);
                                } else {
                                    generateHolograms = true;
                                }
                            }


                            if (Main.dbEnabled) {
                                if (PressurePlateInteractListener.getStart() == null) {
                                    Main.db.addLocation(((Player) sender).getLocation().getBlock().getLocation(), 0, -1);
                                } else {
                                    Main.db.setLocation(((Player) sender).getLocation().getBlock().getLocation(), 0, -1);
                                }
                                if (Main.isHologramsActive()) {
                                    if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                        if (generateHolograms) {
                                            Main.generateHolograms(true);
                                        }
                                    }
                                }
                            } else {
                                Main.addStorage("spawn.location", ((Player) sender).getLocation().getBlock().getLocation());
                                if (Main.isHologramsActive()) {
                                    if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                        if (generateHolograms) {
                                            Main.generateHolograms(false);
                                        }
                                    }
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
                            boolean generateHolograms = false;
                            Location l = ((Player) sender).getLocation().getBlock().getLocation();
                            if (PressurePlateInteractListener.getStart() != null && PressurePlateInteractListener.getRestart() != null) {
                                if (getEnd() != null) {
                                    Hologram h = Main.getHolograms().get(1);
                                    h.delete();
                                    Main.getHolograms().remove(1);
                                    l.setX(l.getX() + 0.5);
                                    l.setZ(l.getZ() + 0.5);
                                    l.setY(l.getY() + 2);
                                    Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Holograms.End")));
                                    Main.getHolograms().add(1, hologram);
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    l.setY(l.getY() - 2);
                                } else {
                                    generateHolograms = true;
                                }
                            }

                            if (Main.dbEnabled) {
                                if (PressurePlateInteractListener.getEnd() == null) {
                                    Main.db.addLocation(((Player) sender).getLocation().getBlock().getLocation(), 1, -1);
                                } else {
                                    Main.db.setLocation(((Player) sender).getLocation().getBlock().getLocation(), 1, -1);
                                }

                                if (Main.isHologramsActive()) {
                                    if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                        if (generateHolograms) {
                                            Main.generateHolograms(true);
                                        }
                                    }
                                }
                            } else {
                                Main.addStorage("end.location", ((Player) sender).getLocation().getBlock().getLocation());
                                if (Main.isHologramsActive()) {
                                    if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                        if (generateHolograms) {
                                            Main.generateHolograms(false);
                                        }
                                    }
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
                            if (PressurePlateInteractListener.getRestart() == null) {
                                Main.db.addLocation(l, 2, -1);
                            } else {
                                Main.db.setLocation(l, 2, -1);
                            }
                        } else {
                            Main.addStorage("reset.location", l);
                        }

                        if (PressurePlateInteractListener.getStart() != null && PressurePlateInteractListener.getEnd() != null && PressurePlateInteractListener.getRestart() == null) {
                            if (Main.dbEnabled) {
                                Main.generateHolograms(true);
                            } else {
                                Main.generateHolograms(false);
                            }
                        }

                        PressurePlateInteractListener.setRestart(l);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetRestart.Successful")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Permission")));
                    }
                    break;
                case "setcheck":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        if (((Player) sender).getLocation().getBlock().getType().equals(PressurePlateInteractListener.getCheckType())) {
                            try {
                                int i = Integer.parseInt(args[1]);
                            } catch(NumberFormatException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Number-Format-Error")));
                                return true;
                            } catch(ArrayIndexOutOfBoundsException e) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Invalid-Arguments")));
                                return true;
                            }
                            if (Integer.parseInt(args[1])-1 > getCheckLocations().size()) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Not-Enough-Checkpoints").replace("{checkpoint}",args[1]).replace("{set-checkpoint}", Integer.toString(getCheckLocations().size() + 1))));
                                return true;
                            }
                            boolean generateHolograms = false;
                            Location l = ((Player) sender).getLocation().getBlock().getLocation();
                            if (PressurePlateInteractListener.getEnd() != null && PressurePlateInteractListener.getRestart() != null && PressurePlateInteractListener.getStart() != null) {
                                if (getCheck(Integer.parseInt(args[1])) != null) {
                                    Hologram h = Main.getHolograms().get(Integer.parseInt(args[1]) + 1);
                                    h.delete();
                                    Main.getHolograms().remove(Integer.parseInt(args[1]) + 1);
                                    l.setX(l.getX() + 0.5);
                                    l.setZ(l.getZ() + 0.5);
                                    l.setY(l.getY() + 2);
                                    Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Holograms.Checkpoint")).replace("{checkpoint}", args[1]));
                                    Main.getHolograms().add(Integer.parseInt(args[1]) + 1, hologram);
                                    l.setX(l.getX() - 0.5);
                                    l.setZ(l.getZ() - 0.5);
                                    l.setY(l.getY() - 2);
                                    getCheckLocations().remove(Integer.parseInt(args[1]) - 1);
                                } else {
                                    generateHolograms = true;
                                }
                            }
                            if (Main.dbEnabled) {
                                if (PressurePlateInteractListener.getCheck(Integer.parseInt(args[1])) != null) {
                                    Main.db.setLocation(((Player) sender).getLocation().getBlock().getLocation(), 3, Integer.parseInt(args[1]));
                                } else {
                                    Main.db.addLocation(((Player) sender).getLocation().getBlock().getLocation(), 3, Integer.parseInt(args[1]));
                                }
                            } else {
                                Main.addStorage(args[1] + ".location", ((Player) sender).getLocation().getBlock().getLocation());
                            }

                            if (Main.isHologramsActive()) {
                                if (Main.getMainConfig().getBoolean("Settings.Holograms")) {
                                    if (generateHolograms) {
                                        l.setY(l.getY() + 2);
                                        l.setX(l.getX() + 0.5);
                                        l.setZ(l.getZ() + 0.5);
                                        Hologram hologram = HologramsAPI.createHologram(Main.get(), l);
                                        TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Holograms.Checkpoint")).replace("{checkpoint}", args[1]));
                                        l.setX(l.getX() - 0.5);
                                        l.setZ(l.getZ() - 0.5);
                                        l.setY(l.getY() - 2);
                                        Main.getHolograms().add(Integer.parseInt(args[1]) + 1, hologram);
                                    }
                                }
                            }
                            getCheckLocations().add(Integer.parseInt(args[1]) - 1, l);
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
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.getMainConfig().getString("Messages.Commands.Leave.Not-In-Parkour")));
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
