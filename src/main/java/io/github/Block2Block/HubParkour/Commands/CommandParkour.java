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
                    if (PressurePlateInteractListener.getParkourPlayers().contains((Player) sender)) {
                        Location l = (Location) Main.getStorage().get("spawn.location");
                        ((Player) sender).teleport(l);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Reset.Successful")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Reset.Not-Started-Parkour")));
                    }
                    break;
                case "checkpoint":
                    if (PressurePlateInteractListener.getParkourPlayers().contains((Player) sender)) {
                        if (PressurePlateInteractListener.getParkourChecks().get((Player) sender).equals(0)) {
                            Location l = (Location) Main.getStorage().get("spawn.location");
                            ((Player) sender).teleport(l);
                        } else {
                            Location l = (Location) Main.getStorage().get(PressurePlateInteractListener.getParkourChecks().get((Player) sender) + ".location");
                            ((Player) sender).teleport(l);
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Checkpoint.Successful")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Checkpoint.Not-Started-Parkour")));
                    }
                    break;
                case "top3":
                case "leaderboard":
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Top3").replace("{player1-name}",Main.getLeaderboard().getString("leaderboard.1.PlayerName")).replace("{player1-time}", Main.getLeaderboard().getString("leaderboard.1.Time")).replace("{player2-name}",Main.getLeaderboard().getString("leaderboard.2.PlayerName")).replace("{player2-time}", Main.getLeaderboard().getString("leaderboard.2.Time")).replace("{player3-name}",Main.getLeaderboard().getString("leaderboard.3.PlayerName")).replace("{player3-time}", Main.getLeaderboard().getString("leaderboard.3.Time"))));
                    break;
                case "setstart":
                    if (((Player) sender).hasPermission("hubparkour.admin")) {
                        if (((Player) sender).getLocation().getBlock().getType().equals(Material.WOOD_PLATE)) {
                            Main.addStorage("spawn.location", ((Player) sender).getLocation().getBlock().getLocation());
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
                            PressurePlateInteractListener.setStart(l);
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
                        if (((Player) sender).getLocation().getBlock().getType().equals(Material.IRON_PLATE)) {
                            Main.addStorage("end.location", ((Player) sender).getLocation().getBlock().getLocation());
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
                            PressurePlateInteractListener.setEnd(l);
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
                        Main.addStorage("reset.location", l);
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
                            }
                            Main.addStorage(args[1] + ".location", ((Player) sender).getLocation().getBlock().getLocation());
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
                            PressurePlateInteractListener.getCheckLocations().add(l);
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Successful").replace("{checkpoint}",args[1])));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.Admin.SetCheck.Invalid-Location")));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getMainConfig().getString("Messages.Commands.No-Permission")));
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
