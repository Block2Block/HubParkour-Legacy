package io.github.Block2Block.HubParkour.Managers;

import io.github.Block2Block.HubParkour.Main;

public class ConfigParser {

    public boolean parseExist() {
        //Messages
        if(!Main.getMainConfig().contains("Messages.Join-Message")) {Main.getMainConfig().set("Messages.Join-Message", "&2Parkour>> &7This server uses HubParkour by Block2Block.");}
        if(!Main.getMainConfig().contains("Messages.Holograms.Start")) Main.getMainConfig().set("Messages.Holograms.Start", "&9&lParkour Start");
        if(!Main.getMainConfig().contains("Messages.Holograms.End")) Main.getMainConfig().set("Messages.Holograms.End", "&9&lParkour End");
        if(!Main.getMainConfig().contains("Messages.Holograms.Checkpoint")) Main.getMainConfig().set("Messages.Holograms.Checkpoint", "&9&lCheckpoint #{checkpoint}");
        if(!Main.getMainConfig().contains("Messages.Commands.No-Arguments")) Main.getMainConfig().set("Messages.Commands.No-Arguments", "&2Parkour>> &7Parkour Help:\n&a/parkour reset &7- Sends you back to the start.\n&a/parkour checkpoint &7- Teleports you to the last checkpoint you reached.\n&a/parkour leave &7- Makes you leave the parkour.");
        if(!Main.getMainConfig().contains("Messages.Commands.Unknown-Subcommand")) Main.getMainConfig().set("Messages.Commands.Unknown-Subcommand", "&2Parkour>> &7That sub-command is unknown!");
        if(!Main.getMainConfig().contains("Messages.Commands.No-Permission")) Main.getMainConfig().set("Messages.Commands.No-Permission", "&2Parkour>> &7You do not have permission to execute this command.");
        if(!Main.getMainConfig().contains("Messages.Commands.Reset.Successful")) Main.getMainConfig().set("Messages.Commands.Reset.Successful", "&2Parkour>> &7You have been teleported to the start.");
        if(!Main.getMainConfig().contains("Messages.Commands.Reset.Not-Started-Parkour")) Main.getMainConfig().set("Messages.Commands.Reset.Not-Started-Parkour", "&2Parkour>> &7You must start the parkour in order to reset!");
        if(!Main.getMainConfig().contains("Messages.Commands.Checkpoint.Successful")) Main.getMainConfig().set("Messages.Commands.Checkpoint.Successful", "&2Parkour>> &7You have been teleported to your last checkpoint.");
        if(!Main.getMainConfig().contains("Messages.Commands.Checkpoint.Not-Started-Parkour")) Main.getMainConfig().set("Messages.Commands.Checkpoint.Not-Started-Parkour", "&2Parkour>> &7You must start the parkour in order to teleport to a checkpoint!");
        if(!Main.getMainConfig().contains("Messages.Commands.Top3")) Main.getMainConfig().set("Messages.Commands.Top3", "&2Parkour>> &7The top 3 times are:\n&a#1 &7- &a{player1-name} &7- &a{player1-time} &7seconds.\n&a#2 &7- &a{player2-name} &7- &a{player2-time} &7seconds.\n&a#3 &7- &a{player3-name} &7- &a{player3-time} &7seconds.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.Reload.Successful")) Main.getMainConfig().set("Messages.Commands.Admin.Reload.Successful", "&2Parkour>> &7Reload successful!");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.Reload.Failed")) Main.getMainConfig().set("Messages.Commands.Admin.Reload.Failed", "&2Parkour>> &7The reload failed. Please make sure that you have formatted the file correctly.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetStart.Successful")) Main.getMainConfig().set("Messages.Commands.Admin.SetStart.Successful", "&2Parkour>> &7You have successfully set the startpoint.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetStart.Invalid-Location")) Main.getMainConfig().set("Messages.Commands.Admin.SetStart.Invalid-Location", "&2Parkour>> &7That location is not valid! Please stand over a wooden pressure plate to set the spawn.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetEnd.Successful")) Main.getMainConfig().set("Messages.Commands.Admin.SetEnd.Successful", "&2Parkour>> &7You have successfully set the endpoint.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetEnd.Invalid-Location")) Main.getMainConfig().set("Messages.Commands.Admin.SetEnd.Invalid-Location", "&2Parkour>> &7That location is not valid! Please stand over an iron pressure plate to set the send.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetRestart.Successful")) Main.getMainConfig().set("Messages.Commands.Admin.SetRestart.Successful", "&2Parkour>> &7You have successfully set the restart point.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetCheck.Successful")) Main.getMainConfig().set("Messages.Commands.Admin.SetCheck.Successful", "&2Parkour>> &7You have successfully set checkpoint #{checkpoint}.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetCheck.Invalid-Location")) Main.getMainConfig().set("Messages.Commands.Admin.SetCheck.Invalid-Location", "&2Parkour>> &7That location is not valid! Please stand over an gold pressure plate to set a checkpoint.");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetCheck.Number-Format-Error")) Main.getMainConfig().set("Messages.Commands.Admin.SetCheck.Number-Format-Error", "&2Parkour>> &7Sorry, that is not a valid checkpoint!");
        if(!Main.getMainConfig().contains("Messages.Commands.Admin.SetCheck.InvalidArguments")) Main.getMainConfig().set("Messages.Commands.Admin.SetCheck.InvalidArguments", "&2Parkour>> &7Invalid Arguments. Correct Usage: &a/parkour setcheck {checkpoint}");
        if(!Main.getMainConfig().contains("Messages.Parkour.Started")) Main.getMainConfig().set("Messages.Parkour.Started", "&2Parkour>> &7You have started the parkour!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Restarted")) Main.getMainConfig().set("Messages.Parkour.Restarted", "&2Parkour>> &7You have restarted the parkour! Your time has been reset to 0!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Checkpoints.Reached")) Main.getMainConfig().set("Messages.Parkour.Checkpoints.Reached", "&2Parkour>> &7You have reached checkpoint #{checkpoint}! You have received $100 as a reward!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Checkpoints.Not-Started")) Main.getMainConfig().set("Messages.Parkour.Checkpoints.Not-Started", "&2Parkour>> &7You must start the parkour in order to reach checkpoints!");
        if(!Main.getMainConfig().contains("Messages.Parkour.End.Beat-Previous-Personal-Best")) Main.getMainConfig().set("Messages.Parkour.End.Beat-Previous-Personal-Best", "&2Parkour>> &7You beat your previous record and you managed to complete the parkour in {time} seconds! You have received $100 for finishing the parkour!");
        if(!Main.getMainConfig().contains("Messages.Parkour.End.Not-Beat-Previous-Personal-Best")) Main.getMainConfig().set("Messages.Parkour.End.Not-Beat-Previous-Personal-Best", "&2Parkour>> &7You didn't beat your previous record, but you managed to complete the parkour in {time} seconds! You have received $100 for finishing the parkour!");
        if(!Main.getMainConfig().contains("Messages.Parkour.End.First-Time")) Main.getMainConfig().set("Messages.Parkour.End.First-TIme", "&2Parkour>> &7Well done! You completed the parkour in {time} seconds! You received $1000 as a reward for completing the parkour for the first time!");
        if(!Main.getMainConfig().contains("Messages.Parkour.End.Not-Started")) Main.getMainConfig().set("Messages.Parkour.End.Not-Started", "&2Parkour>> &7You must start the parkour in order to finish it.");
        if(!Main.getMainConfig().contains("Messages.Parkour.End.Failed.Fly")) Main.getMainConfig().set("Messages.Parkour.End.Failed.Fly", "&2Parkour>> &7You are not allowed to fly while doing the parkour. Parkour failed!");
        if(!Main.getMainConfig().contains("Messages.Parkour.End.Failed.Not-Enough-Checkpoints")) Main.getMainConfig().set("Messages.Parkour.End.Failed.Not-Enough-Checkpoints", "&2Parkour>> &7You did not reach enough checkpoints, parkour failed!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leaderboard.Still-First")) Main.getMainConfig().set("Messages.Parkour.Leaderboard.Still-First", "&2Parkour>> &7You are still in 1st place!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leaderboard.Now-First")) Main.getMainConfig().set("Messages.Parkour.Leaderboard.Now-First", "&2Parkour>> &7Well done! You have entered 1st place!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leaderboard.Still-Second")) Main.getMainConfig().set("Messages.Parkour.Leaderboard.Still-Second", "&2Parkour>> &7You are still in 2nd place!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leaderboard.Now-Second")) Main.getMainConfig().set("Messages.Parkour.Leaderboard.Now-Second", "&2Parkour>> &7Well done! You have entered 2nd place!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leaderboard.Still-Third")) Main.getMainConfig().set("Messages.Parkour.Leaderboard.Still-Third", "&2Parkour>> &7You are still in 3rd place!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leaderboard.Now-Third")) Main.getMainConfig().set("Messages.Parkour.Leaderboard.Now-Third", "&2Parkour>> &7Well done! You have entered 3rd place!");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leave.Left")) Main.getMainConfig().set("Messages.Parkour.Leave.Left", "&2Parkour>> &7You have left the parkour and your time has been reset.");
        if(!Main.getMainConfig().contains("Messages.Parkour.Leave.Not-In-Parkour")) Main.getMainConfig().set("Messages.Parkour.Leave.Not-In-Parkour", "&2Parkour>> &7You must have started the parkour in order to leave it.");


        //Settings
        if(!Main.getMainConfig().contains("Settings.Holograms")) Main.getMainConfig().set("Settings.Holograms", false);
        if(!Main.getMainConfig().contains("Settings.Must-Complete-All-Checkpoints")) Main.getMainConfig().set("Settings.Must-Complete-All-Checkpoints", true);
        if(!Main.getMainConfig().contains("Settings.Join-Message")) Main.getMainConfig().set("Settings.Join-Message", true);
        if(!Main.getMainConfig().contains("Settings.Version-Checker")) {Main.getMainConfig().set("Settings.Version-Checker", true);}
        if(!Main.getMainConfig().contains("Settings.Database.Enabled")) Main.getMainConfig().set("Settings.Database.Enabled", false);
        if(!Main.getMainConfig().contains("Settings.Database.Type")) Main.getMainConfig().set("Settings.Database.Type", "SQLite");
        if(!Main.getMainConfig().contains("Settings.Database.Details.MySQL.Hostname")) Main.getMainConfig().set("Settings.Database.Details.MySQL.Hostname", "localhost");
        if(!Main.getMainConfig().contains("Settings.Database.Details.MySQL.Port")) Main.getMainConfig().set("Settings.Database.Details.MySQL.Port", "3306");
        if(!Main.getMainConfig().contains("Settings.Database.Details.MySQL.Database")) Main.getMainConfig().set("Settings.Database.Details.MySQL.Database", "HubParkour");
        if(!Main.getMainConfig().contains("Settings.Database.Details.MySQL.Username")) Main.getMainConfig().set("Settings.Database.Details.MySQL.Username", "root");
        if(!Main.getMainConfig().contains("Settings.Database.Details.MySQL.Password")) Main.getMainConfig().set("Settings.Database.Details.MySQL.Password", "");
        if(!Main.getMainConfig().contains("Settings.Database.SQLite.File-Name")) Main.getMainConfig().set("Settings.Database.SQLite.File-Name", "hp-storage.db");
        if(!Main.getMainConfig().contains("Settings.Pressure-Plates.Start")) Main.getMainConfig().set("Settings.Pressure-Plates.Start", "Wood");
        if(!Main.getMainConfig().contains("Settings.Pressure-Plates.End")) Main.getMainConfig().set("Settings.Pressure-Plates.End", "Iron");
        if(!Main.getMainConfig().contains("Settings.Pressure-Plates.Checkpoint")) Main.getMainConfig().set("Settings.Pressure-Plates.Checkpoints", "Gold");
        if(!Main.getMainConfig().contains("Settings.Rewards.Checkpoint-Reward.Enabled")) Main.getMainConfig().set("Settings.Rewards.Checkpoint-Reward.Enabled", true);
        if(!Main.getMainConfig().contains("Settings.Rewards.Checkpoint-Reward.Command")) Main.getMainConfig().set("Settings.Rewards.Checkpoint-Reward.Command", "money give {player-name} 100");
        if(!Main.getMainConfig().contains("Settings.Rewards.Finish-Reward.Enabled")) Main.getMainConfig().set("Settings.Rewards.Finish-Reward.Enabled", true);
        if(!Main.getMainConfig().contains("Settings.Rewards.Finish-Reward.First-Time-Only")) Main.getMainConfig().set("Settings.Rewards.Finish-Reward.First-Time-Only", false);
        if(!Main.getMainConfig().contains("Settings.Rewards.Finish-Reward.First-Time-Command")) Main.getMainConfig().set("Settings.Rewards.Finish-Reward.First-Time-Command", "money give {player-name} 1000");
        if(!Main.getMainConfig().contains("Settings.Rewards.Finish-Reward.After-Completed-Command")) Main.getMainConfig().set("Settings.Rewards.Finish-Reward.After-Completed-Command", "money give {player-name} 100");
        return true;
    }

    public boolean parseType() {
        //Messages
        if(!Main.getMainConfig().isString("Messages.Join-Message")) Main.addConfig("Messages.Join-Message", "&2Parkour>> &7This server uses HubParkour by Block2Block.");
        if(!Main.getMainConfig().isString("Messages.Holograms.Start")) Main.addConfig("Messages.Holograms.Start", "&9&lParkour Start");
        if(!Main.getMainConfig().isString("Messages.Holograms.End")) Main.addConfig("Messages.Holograms.End", "&9&lParkour End");
        if(!Main.getMainConfig().isString("Messages.Holograms.Checkpoint")) Main.addConfig("Messages.Holograms.Checkpoint", "&9&lCheckpoint #{checkpoint}");
        if(!Main.getMainConfig().isString("Messages.Commands.No-Arguments")) Main.addConfig("Messages.Commands.No-Arguments", "&2Parkour>> &7Parkour Help:\n&a/parkour reset &7- Sends you back to the start.\n&a/parkour checkpoint &7- Teleports you to the last checkpoint you reached.\n&a/parkour leave &7- Makes you leave the parkour.");
        if(!Main.getMainConfig().isString("Messages.Commands.Unknown-Subcommand")) Main.addConfig("Messages.Commands.Unknown-Subcommand", "&2Parkour>> &7That sub-command is unknown!");
        if(!Main.getMainConfig().isString("Messages.Commands.No-Permission")) Main.addConfig("Messages.Commands.No-Permission", "&2Parkour>> &7You do not have permission to execute this command.");
        if(!Main.getMainConfig().isString("Messages.Commands.Reset.Successful")) Main.addConfig("Messages.Commands.Reset.Successful", "&2Parkour>> &7You have been teleported to the start.");
        if(!Main.getMainConfig().isString("Messages.Commands.Reset.Not-Started-Parkour")) Main.addConfig("Messages.Commands.Reset.Not-Started-Parkour", "&2Parkour>> &7You must start the parkour in order to reset!");
        if(!Main.getMainConfig().isString("Messages.Commands.Checkpoint.Successful")) Main.addConfig("Messages.Commands.Checkpoint.Successful", "&2Parkour>> &7You have been teleported to your last checkpoint.");
        if(!Main.getMainConfig().isString("Messages.Commands.Checkpoint.Not-Started-Parkour")) Main.addConfig("Messages.Commands.Checkpoint.Not-Started-Parkour", "&2Parkour>> &7You must start the parkour in order to teleport to a checkpoint!");
        if(!Main.getMainConfig().isString("Messages.Commands.Top3")) Main.addConfig("Messages.Commands.Top3", "&2Parkour>> &7The top 3 times are:\n&a#1 &7- &a{player1-name} &7- &a{player1-time} &7seconds.\n&a#2 &7- &a{player2-name} &7- &a{player2-time} &7seconds.\n&a#3 &7- &a{player3-name} &7- &a{player3-time} &7seconds.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.Reload.Successful")) Main.addConfig("Messages.Commands.Admin.Reload.Successful", "&2Parkour>> &7Reload successful!");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.Reload.Failed")) Main.addConfig("Messages.Commands.Admin.Reload.Failed", "&2Parkour>> &7The reload failed. Please make sure that you have formatted the file correctly.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetStart.Successful")) Main.addConfig("Messages.Commands.Admin.SetStart.Successful", "&2Parkour>> &7You have successfully set the startpoint.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetStart.Invalid-Location")) Main.addConfig("Messages.Commands.Admin.SetStart.Invalid-Location", "&2Parkour>> &7That location is not valid! Please stand over a wooden pressure plate to set the spawn.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetEnd.Successful")) Main.addConfig("Messages.Commands.Admin.SetEnd.Successful", "&2Parkour>> &7You have successfully set the endpoint.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetEnd.Invalid-Location")) Main.addConfig("Messages.Commands.Admin.SetEnd.Invalid-Location", "&2Parkour>> &7That location is not valid! Please stand over an iron pressure plate to set the send.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetRestart.Successful")) Main.addConfig("Messages.Commands.Admin.SetRestart.Successful", "&2Parkour>> &7You have successfully set the restart point.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetCheck.Successful")) Main.addConfig("Messages.Commands.Admin.SetCheck.Successful", "&2Parkour>> &7You have successfully set checkpoint #{checkpoint}.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetCheck.Invalid-Location")) Main.addConfig("Messages.Commands.Admin.SetCheck.Invalid-Location", "&2Parkour>> &7That location is not valid! Please stand over an gold pressure plate to set a checkpoint.");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetCheck.Number-Format-Error")) Main.addConfig("Messages.Commands.Admin.SetCheck.Number-Format-Error", "&2Parkour>> &7Sorry, that is not a valid checkpoint!");
        if(!Main.getMainConfig().isString("Messages.Commands.Admin.SetCheck.InvalidArguments")) Main.addConfig("Messages.Commands.Admin.SetCheck.InvalidArguments", "&2Parkour>> &7Invalid Arguments. Correct Usage: &a/parkour setcheck {checkpoint}");
        if(!Main.getMainConfig().isString("Messages.Parkour.Started")) Main.addConfig("Messages.Parkour.Started", "&2Parkour>> &7You have started the parkour!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Restarted")) Main.addConfig("Messages.Parkour.Restarted", "&2Parkour>> &7You have restarted the parkour! Your time has been reset to 0!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Checkpoints.Reached")) Main.addConfig("Messages.Parkour.Checkpoints.Reached", "&2Parkour>> &7You have reached checkpoint #{checkpoint}! You have received $100 as a reward!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Checkpoints.Not-Started")) Main.addConfig("Messages.Parkour.Checkpoints.Not-Started", "&2Parkour>> &7You must start the parkour in order to reach checkpoints!");
        if(!Main.getMainConfig().isString("Messages.Parkour.End.Beat-Previous-Personal-Best")) Main.addConfig("Messages.Parkour.End.Beat-Previous-Personal-Best", "&2Parkour>> &7You beat your previous record and you managed to complete the parkour in {time} seconds! You have received $100 for finishing the parkour!");
        if(!Main.getMainConfig().isString("Messages.Parkour.End.Not-Beat-Previous-Personal-Best")) Main.addConfig("Messages.Parkour.End.Not-Beat-Previous-Personal-Best", "&2Parkour>> &7You didn't beat your previous record, but you managed to complete the parkour in {time} seconds! You have received $100 for finishing the parkour!");
        if(!Main.getMainConfig().isString("Messages.Parkour.End.First-Time")) Main.addConfig("Messages.Parkour.End.First-TIme", "&2Parkour>> &7Well done! You completed the parkour in {time} seconds! You received $1000 as a reward for completing the parkour for the first time!");
        if(!Main.getMainConfig().isString("Messages.Parkour.End.Not-Started")) Main.addConfig("Messages.Parkour.End.Not-Started", "&2Parkour>> &7You must start the parkour in order to finish it.");
        if(!Main.getMainConfig().isString("Messages.Parkour.End.Failed.Fly")) Main.addConfig("Messages.Parkour.End.Failed.Fly", "&2Parkour>> &7You are not allowed to fly while doing the parkour. Parkour failed!");
        if(!Main.getMainConfig().isString("Messages.Parkour.End.Failed.Not-Enough-Checkpoints")) Main.addConfig("Messages.Parkour.End.Failed.Not-Enough-Checkpoints", "&2Parkour>> &7You did not reach enough checkpoints, parkour failed!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leaderboard.Still-First")) Main.addConfig("Messages.Parkour.Leaderboard.Still-First", "&2Parkour>> &7You are still in 1st place!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leaderboard.Now-First")) Main.addConfig("Messages.Parkour.Leaderboard.Now-First", "&2Parkour>> &7Well done! You have entered 1st place!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leaderboard.Still-Second")) Main.addConfig("Messages.Parkour.Leaderboard.Still-Second", "&2Parkour>> &7You are still in 2nd place!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leaderboard.Now-Second")) Main.addConfig("Messages.Parkour.Leaderboard.Now-Second", "&2Parkour>> &7Well done! You have entered 2nd place!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leaderboard.Still-Third")) Main.addConfig("Messages.Parkour.Leaderboard.Still-Third", "&2Parkour>> &7You are still in 3rd place!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leaderboard.Now-Third")) Main.addConfig("Messages.Parkour.Leaderboard.Now-Third", "&2Parkour>> &7Well done! You have entered 3rd place!");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leave.Left")) Main.addConfig("Messages.Parkour.Leave.Left", "&2Parkour>> &7You have left the parkour and your time has been reset.");
        if(!Main.getMainConfig().isString("Messages.Parkour.Leave.Not-In-Parkour")) Main.addConfig("Messages.Parkour.Leave.Not-In-Parkour", "&2Parkour>> &7You must have started the parkour in order to leave it.");


        //Settings
        if(!Main.getMainConfig().isBoolean("Settings.Holograms")) Main.addConfig("Settings.Holograms", false);
        if(!Main.getMainConfig().isBoolean("Settings.Must-Complete-All-Checkpoints")) Main.addConfig("Settings.Must-Complete-All-Checkpoints", true);
        if(!Main.getMainConfig().isBoolean("Settings.Join-Message")) Main.addConfig("Settings.Join-Message", true);
        if(!Main.getMainConfig().isBoolean("Settings.Version-Checker")) {Main.addConfig("Settings.Version-Checker", true);}
        if(!Main.getMainConfig().isBoolean("Settings.Database.Enabled")) Main.addConfig("Settings.Database.Enabled", false);
        if(!Main.getMainConfig().isString("Settings.Database.Type")) Main.addConfig("Settings.Database.Type", "SQLite");
        if(!Main.getMainConfig().isString("Settings.Database.Details.MySQL.Hostname")) Main.addConfig("Settings.Database.Details.MySQL.Hostname", "localhost");
        if(!Main.getMainConfig().isString("Settings.Database.Details.MySQL.Port")) Main.addConfig("Settings.Database.Details.MySQL.Port", "3306");
        if(!Main.getMainConfig().isString("Settings.Database.Details.MySQL.Database")) Main.addConfig("Settings.Database.Details.MySQL.Database", "HubParkour");
        if(!Main.getMainConfig().isString("Settings.Database.Details.MySQL.Username")) Main.addConfig("Settings.Database.Details.MySQL.Username", "root");
        if(!Main.getMainConfig().isString("Settings.Database.Details.MySQL.Password")) Main.addConfig("Settings.Database.Details.MySQL.Password", "");
        if(!Main.getMainConfig().isString("Settings.Database.SQLite.File-Name")) Main.addConfig("Settings.Database.SQLite.File-Name", "hp-storage.db");
        if(!Main.getMainConfig().isString("Settings.Pressure-Plates.Start")) Main.addConfig("Settings.Pressure-Plates.Start", "Wood");
        if(!Main.getMainConfig().isString("Settings.Pressure-Plates.End")) Main.addConfig("Settings.Pressure-Plates.End", "Iron");
        if(!Main.getMainConfig().isString("Settings.Pressure-Plates.Checkpoint")) Main.addConfig("Settings.Pressure-Plates.Checkpoints", "Gold");
        if(!Main.getMainConfig().isBoolean("Settings.Rewards.Checkpoint-Reward.Enabled")) Main.addConfig("Settings.Rewards.Checkpoint-Reward.Enabled", true);
        if(!Main.getMainConfig().isString("Settings.Rewards.Checkpoint-Reward.Command")) Main.addConfig("Settings.Rewards.Checkpoint-Reward.Command", "money give {player-name} 100");
        if(!Main.getMainConfig().isBoolean("Settings.Rewards.Finish-Reward.Enabled")) Main.addConfig("Settings.Rewards.Finish-Reward.Enabled", true);
        if(!Main.getMainConfig().isBoolean("Settings.Rewards.Finish-Reward.First-Time-Only")) Main.addConfig("Settings.Rewards.Finish-Reward.First-Time-Only", false);
        if(!Main.getMainConfig().isString("Settings.Rewards.Finish-Reward.First-Time-Command")) Main.addConfig("Settings.Rewards.Finish-Reward.First-Time-Command", "money give {player-name} 1000");
        if(!Main.getMainConfig().isString("Settings.Rewards.Finish-Reward.After-Completed-Command")) Main.addConfig("Settings.Rewards.Finish-Reward.After-Completed-Command", "money give {player-name} 100");

        //Checking values are not blank.
        if(!Main.getMainConfig().getString("Settings.Database.SQLite.File-Name").equalsIgnoreCase("")) Main.addConfig("Settings.Database.SQLite.File-Name", "hp-storage.db");
        if(!Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes").equalsIgnoreCase("")) Main.addConfig("Settings.Database.Tables.PlayerTimes", "hp_playertimes");
        if(!Main.getMainConfig().getString("Settings.Database.Tables.Locations").equals("")) Main.addConfig("Settings.Database.Tables.Locations", "hp_locations");

        return true;
    }

}
