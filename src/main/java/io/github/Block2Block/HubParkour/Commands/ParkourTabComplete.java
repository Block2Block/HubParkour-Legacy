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

                list.add("restart");
                list.add("checkpoint");
                list.add("leaderboard");
                list.add("leave");

                if (p.hasPermission("hubparkour.admin")) {
                    list.add("setstart");
                    list.add("setend");
                    list.add("setrestart");
                    list.add("setcheck");
                    list.add("reload");
                }

                Collections.sort(list);

                return list;

            }
        }

        return null;
    }
}
