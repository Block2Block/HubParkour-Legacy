package io.github.Block2Block.HubParkour.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkourTabComplete implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("parkour") && args.length == 1) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                List<String> list = new ArrayList<>();

                if ("restart".startsWith(args[0])) {
                    list.add("restart");
                }

                if ("checkpoint".startsWith(args[0])) {
                    list.add("checkpoint");
                }

                if ("leaderboard".startsWith(args[0])) {
                    list.add("leaderboard");
                }

                if ("leave".startsWith(args[0])) {
                    list.add("leave");
                }


                if (p.hasPermission("hubparkour.admin")) {
                    if ("setstart".startsWith(args[0])) {
                        list.add("setstart");
                    }

                    if ("setend".startsWith(args[0])) {
                        list.add("setend");
                    }

                    if ("setrestart".startsWith(args[0])) {
                        list.add("setrestart");
                    }

                    if ("setcheck".startsWith(args[0])) {
                        list.add("setcheck");
                    }

                    if ("reload".startsWith(args[0])) {
                        list.add("reload");
                    }

                }


                Collections.sort(list);

                return list;

            }
        }

        return null;
    }
}
