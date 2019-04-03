package io.github.Block2Block.HubParkour;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import io.github.Block2Block.HubParkour.Commands.CommandParkour;
import io.github.Block2Block.HubParkour.Commands.ParkourTabComplete;
import io.github.Block2Block.HubParkour.Listeners.*;
import io.github.Block2Block.HubParkour.Managers.ConfigParser;
import io.github.Block2Block.HubParkour.MySQL.DatabaseManager;
import org.apache.commons.io.IOUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {

    private static File configFile;
    private static FileConfiguration config;
    private static File storageFile;
    private static FileConfiguration storage;
    private static File leaderboardFile;
    private static FileConfiguration leaderboard;

    private static List<Hologram> holograms = new ArrayList<>();
    private static boolean useHolographicDisplays;

    private static Main instance;

    public static Main get() {
        return instance;
    }

    public static boolean isHologramsActive() {
        return useHolographicDisplays;
    }

    public static DatabaseManager db;
    public static boolean dbEnabled;

    @Override
    public void onEnable() {
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
        loadConfigs();
        registerListeners(new LeaveListener(), new PlayerToggleFlyListener(), new PressurePlateInteractListener(), new JoinListener());
        getCommand("parkour").setExecutor(new CommandParkour());
        getCommand("parkour").setTabCompleter(new ParkourTabComplete());
        getLogger().info("HubParkour has been successfully enabled.");
        if (!useHolographicDisplays) {
            getLogger().info("HolographicDisplays not found.");
        } else {
            getLogger().info("HolographicDisplays has been detected.");
            if (getMainConfig().getBoolean("Settings.Holograms")) {
                if (getMainConfig().getBoolean("Settings.Database.Enabled")) {
                    dbEnabled = true;
                    if (getMainConfig().getString("Settings.Database.Type").equalsIgnoreCase("mysql")) {
                        db = new DatabaseManager(true);
                        List<List<String>> locations = db.getLocations();
                        boolean start = false;
                        boolean end = false;
                        boolean reset = false;
                        for (List<String> x : locations) {
                            switch (Integer.parseInt(x.get(0))) {
                                case 0:
                                    start = true;
                                    break;
                                case 1:
                                    end = true;
                                    break;
                                case 2:
                                    reset = true;
                                    break;
                                case 3:
                                    continue;
                            }
                        }
                        if (start && end && reset) {
                            generateHolograms(true);
                        } else {
                            getLogger().info("The parkour has not been setup yet, the Holograms have not been generated.");
                        }
                    } else if (!getMainConfig().getString("Settings.Database.Type").equalsIgnoreCase("sqlite")) {
                        dbEnabled = false;
                        getLogger().info("There is an issue in the config.yml file. Please ensure that you have selected the right type of Database selected. File storage was selected as a failsafe.");
                        if (getStorage().isSet("spawn.location")&&getStorage().isSet("end.location")&&getStorage().isSet("reset.location")) {
                            generateHolograms(false);
                        } else {
                            getLogger().info("The parkour has not been setup yet, the Holograms have not been generated.");
                        }
                    } else {
                        db = new DatabaseManager(false);
                        List<List<String>> locations = db.getLocations();
                        boolean start = false;
                        boolean end = false;
                        boolean reset = false;
                        for (List<String> x : locations) {
                            switch (Integer.parseInt(x.get(0))) {
                                case 0:
                                    start = true;
                                    break;
                                case 1:
                                    end = true;
                                    break;
                                case 2:
                                    reset = true;
                                    break;
                                case 3:
                                    continue;
                            }
                        }
                        if (start && end && reset) {
                            generateHolograms(true);
                        } else {
                            getLogger().info("The parkour has not been setup yet, the Holograms have not been generated.");
                        }
                    }
                } else {
                    if (getStorage().isSet("spawn.location")&&getStorage().isSet("end.location")&&getStorage().isSet("reset.location")) {
                        generateHolograms(false);
                    } else {
                        getLogger().info("The parkour has not been setup yet, the Holograms have not been generated.");
                    }
                }

            }
        }
        for (String s : getStorage().getKeys(false)) {
            if (s.equalsIgnoreCase("spawn")) {
                Location l = (Location) getStorage().get("spawn.location");
                PressurePlateInteractListener.setStart(l);
            } else if (s.equalsIgnoreCase("end")) {
                Location l = (Location) getStorage().get("end.location");
                PressurePlateInteractListener.setEnd(l);
            } else if (s.equalsIgnoreCase("reset")) {
                continue;
            } else {
                Location l = (Location) getStorage().get(s + ".location");
                if (!PressurePlateInteractListener.getCheckLocations().contains(l)) {
                    PressurePlateInteractListener.getCheckLocations().add(l);
                }
            }
        }
        if (getMainConfig().getBoolean("Settings.Version-Checker")) {
            String version = newVersionCheck();
            if (!version.equals(null)) {
                getLogger().info("HubParkour v" + version + " is out now! I highly recommend you download the new version!");
            } else {
                getLogger().info("Your HubParkour version is up to date!");
            }
        }
    }

    public String newVersionCheck() {
        try {
            String oldVersion = this.getDescription().getVersion();
            String newVersion = fetchSpigotVersion();
            if(!newVersion.equals(oldVersion)) {
                return newVersion;
            }
            return null;
        }
        catch(Exception e) {
            getLogger().info("Unable to check for new versions.");
        }
        return null;
    }

    private String fetchSpigotVersion() {
        try {
            // We're connecting to spigot's API
            URL url = new URL("https://www.spigotmc.org/api/general.php");
            // Creating a connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // We're writing a body that contains the API access key (Not required and obsolete, but!)
            con.setDoOutput(true);

            // Can't think of a clean way to represent this without looking bad
            String body = "key" + "=" + "98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4" + "&" +
                    "resource=47713";

            // Get the output stream, what the site receives
            try (OutputStream stream = con.getOutputStream()) {
                // Write our body containing version and access key
                stream.write(body.getBytes(StandardCharsets.UTF_8));
            }

            // Get the input stream, what we receive
            try (InputStream input = con.getInputStream()) {
                // Read it to string
                String version = IOUtils.toString(input);

                // If the version is not empty, return it
                if (!version.isEmpty()) {
                    return version;
                }
            }
        }
        catch (Exception ex) {
            Bukkit.getLogger().warning("Failed to check for a update on spigot.");
        }

        return null;
    }

    @Override
    public void onDisable() {
        saveYamls();
        getLogger().info("HubParkour has successfully been disabled.");
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        for (TimerEvent.TimerType t : TimerEvent.TimerType.values()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    getServer().getPluginManager().callEvent(new TimerEvent(t));
                }
            }.runTaskTimer(this, 0, t.getTicks());
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadConfigs() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        configFile = new File(getDataFolder(), "config.yml");
        storageFile = new File(getDataFolder(), "storage.yml");
        leaderboardFile = new File(getDataFolder(), "leaderboard.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            copy(getResource("config.yml"), configFile);
        }
        if (!storageFile.exists()) {
            storageFile.getParentFile().mkdirs();
            copy(getResource("storage.yml"), storageFile);
        }
        if (!leaderboardFile.exists()) {
            leaderboardFile.getParentFile().mkdirs();
            copy(getResource("leaderboard.yml"), leaderboardFile);
        }
        config = new YamlConfiguration();
        storage = new YamlConfiguration();
        leaderboard = new YamlConfiguration();
        loadYamls();
        getLogger().info("The config has been loaded. Any invalid or missing values have been added in or corrected.");
        switch (config.getString("Settings.Pressure-Plates.Start").toLowerCase()) {
            case "wood":
                PressurePlateInteractListener.setStartType(Material.WOOD_PLATE);
                break;
            case "gold":
                PressurePlateInteractListener.setStartType(Material.GOLD_PLATE);
                break;
            case "iron":
                PressurePlateInteractListener.setStartType(Material.IRON_PLATE);
                break;
            case "stone":
                PressurePlateInteractListener.setStartType(Material.STONE_PLATE);
                break;
            default:
                getLogger().info("Invalid pressure plate type detected in the config file. The setting has been reset.");
                break;
        }
        switch (config.getString("Settings.Pressure-Plates.End").toLowerCase()) {
            case "wood":
                PressurePlateInteractListener.setEndType(Material.WOOD_PLATE);
                break;
            case "gold":
                PressurePlateInteractListener.setEndType(Material.GOLD_PLATE);
                break;
            case "iron":
                PressurePlateInteractListener.setEndType(Material.IRON_PLATE);
                break;
            case "stone":
                PressurePlateInteractListener.setEndType(Material.STONE_PLATE);
                break;
            default:
                getLogger().info("Invalid pressure plate type detected in the config file. The setting has been reset.");
                break;
        }
        switch (config.getString("Settings.Pressure-Plates.Checkpoint").toLowerCase()) {
            case "wood":
                PressurePlateInteractListener.setCheckType(Material.WOOD_PLATE);
                break;
            case "gold":
                PressurePlateInteractListener.setCheckType(Material.GOLD_PLATE);
                break;
            case "iron":
                PressurePlateInteractListener.setCheckType(Material.IRON_PLATE);
                break;
            case "stone":
                PressurePlateInteractListener.setCheckType(Material.STONE_PLATE);
                break;
            default:
                getLogger().info("Invalid pressure plate type detected in the config file. The setting has been reset.");
                break;
        }
    }

    public static FileConfiguration getMainConfig() {
        return config;
    }

    public static FileConfiguration getStorage() {
        return storage;
    }

    public static FileConfiguration getLeaderboard() {
        return leaderboard;
    }

    public static List<Hologram> getHolograms() { return holograms; }

    public static void loadYamls() {
        try {
            config.load(configFile);
            storage.load(storageFile);
            leaderboard.load(leaderboardFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reloadYamls() {
        try {
            config.load(configFile);
            storage.load(storageFile);
            leaderboard.load(leaderboardFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveYamls() {

        if (leaderboard.get("leaderboard.1.UUID").equals("N/A")) {
            leaderboard.set("leaderboard.1.UUID", "N/A");
            leaderboard.set("leaderboard.1.PlayerName", "No-one");
            leaderboard.set("leaderboard.1.Time", 2147483646);
        }
        if (leaderboard.get("leaderboard.2.UUID").equals("N/A")) {
            leaderboard.set("leaderboard.2.UUID", "N/A");
            leaderboard.set("leaderboard.2.PlayerName", "No-one");
            leaderboard.set("leaderboard.2.Time", 2147483646);
        }
        if (leaderboard.get("leaderboard.3.UUID").equals("N/A")) {
            leaderboard.set("leaderboard.3.UUID", "N/A");
            leaderboard.set("leaderboard.3.PlayerName", "No-one");
            leaderboard.set("leaderboard.3.Time", 2147483646);
        }

        try {
            config.save(configFile);
            config = YamlConfiguration.loadConfiguration(configFile);
            leaderboard.save(leaderboardFile);
            leaderboard = YamlConfiguration.loadConfiguration(leaderboardFile);
            storage.save(storageFile);
            storage = YamlConfiguration.loadConfiguration(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addConfig(String path, Object value) {
        config.set(path, value);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void addLeaderboard(String path, Object value) {
        leaderboard.set(path, value);
        try {
            leaderboard.save(leaderboardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        leaderboard = YamlConfiguration.loadConfiguration(leaderboardFile);
    }

    public static void addStorage(String path, Object value) {
        storage.set(path, value);
        try {
            storage.save(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        storage = YamlConfiguration.loadConfiguration(storageFile);
    }



    private void generateHolograms(boolean isDatabase) {
        if (isDatabase) {
            List<List<String>> locations = db.getLocations();
            for (List<String> x : locations) {
                switch (Integer.parseInt(x.get(0))) {
                    case 0:
                        World world = Bukkit.getWorld(x.get(5));
                        if (world.equals(null)) {
                            getLogger().info("One of the worlds you created the parkour in does not exist. Please reset your start position.");
                            return;
                        }
                        Location l = new Location(world, Integer.parseInt(x.get(1)), Integer.parseInt(x.get(2)), Integer.parseInt(x.get(3)));
                        l.setX(l.getX() + 0.5);
                        l.setZ(l.getZ() + 0.5);
                        l.setY(l.getY() + 2);
                        Hologram hologram = HologramsAPI.createHologram(this, l);
                        TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', getMainConfig().getString("Messages.Holograms.Start")));
                        holograms.add(hologram);
                        l.setX(l.getX() - 0.5);
                        l.setZ(l.getZ() - 0.5);
                        l.setY(l.getY() - 2);
                        PressurePlateInteractListener.setStart(l);
                        break;
                    case 1:
                        World world1 = Bukkit.getWorld(x.get(5));
                        if (world1.equals(null)) {
                            getLogger().info("One of the worlds you created the parkour in does not exist. Please reset your end position.");
                            return;
                        }
                        Location l1 = new Location(world1, Integer.parseInt(x.get(1)), Integer.parseInt(x.get(2)), Integer.parseInt(x.get(3)));
                        l1.setX(l1.getX() + 0.5);
                        l1.setZ(l1.getZ() + 0.5);
                        l1.setY(l1.getY() + 2);
                        Hologram hologram1 = HologramsAPI.createHologram(this, l1);
                        TextLine textLine1 = hologram1.appendTextLine(ChatColor.translateAlternateColorCodes('&', getMainConfig().getString("Messages.Holograms.End")));
                        holograms.add(hologram1);
                        l1.setX(l1.getX() - 0.5);
                        l1.setZ(l1.getZ() - 0.5);
                        l1.setY(l1.getY() - 2);
                        PressurePlateInteractListener.setEnd(l1);
                        break;
                    case 2:
                        World world2 = Bukkit.getWorld(x.get(5));
                        if (world2.equals(null)) {
                            getLogger().info("One of the worlds you created the parkour in does not exist. Please reset your end position.");
                            return;
                        }

                        Location l2 = new Location(world2, Integer.parseInt(x.get(1)), Integer.parseInt(x.get(2)), Integer.parseInt(x.get(3)));
                        PressurePlateInteractListener.setRestart(l2);
                        continue;
                    case 3:

                        World world3 = Bukkit.getWorld(x.get(5));
                        if (world3.equals(null)) {
                            getLogger().info("One of the worlds you created the parkour in does not exist. Please reset your checkpoints.");
                            return;
                        }
                        Location l3 = new Location(world3, Integer.parseInt(x.get(1)), Integer.parseInt(x.get(2)), Integer.parseInt(x.get(3)));
                        l3.setY(l3.getY() + 2);
                        l3.setX(l3.getX() + 0.5);
                        l3.setZ(l3.getZ() + 0.5);
                        Hologram hologram3 = HologramsAPI.createHologram(this, l3);
                        TextLine textLine3 = hologram3.appendTextLine(ChatColor.translateAlternateColorCodes('&', getMainConfig().getString("Messages.Holograms.Checkpoint").replace("{checkpoint}",x.get(4))));
                        holograms.add(hologram3);
                        l3.setY(l3.getY() - 2);
                        l3.setX(l3.getX() - 0.5);
                        l3.setZ(l3.getZ() - 0.5);
                        PressurePlateInteractListener.getCheckLocations().add(l3);
                        PressurePlateInteractListener.setCheck(Integer.parseInt(x.get(4)), l3);

                        break;
                }
            }
        } else {
            for (String s : getStorage().getKeys(false)) {
                if (s.equalsIgnoreCase("spawn")) {
                    Location l = (Location) getStorage().get("spawn.location");
                    l.setX(l.getX() + 0.5);
                    l.setZ(l.getZ() + 0.5);
                    l.setY(l.getY() + 2);
                    Hologram hologram = HologramsAPI.createHologram(this, l);
                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', getMainConfig().getString("Messages.Holograms.Start")));
                    holograms.add(hologram);
                    l.setX(l.getX() - 0.5);
                    l.setZ(l.getZ() - 0.5);
                    l.setY(l.getY() - 2);
                    PressurePlateInteractListener.setStart(l);
                } else if (s.equalsIgnoreCase("end")) {
                    Location l = (Location) getStorage().get("end.location");
                    l.setY(l.getY() + 2);
                    l.setX(l.getX() + 0.5);
                    l.setZ(l.getZ() + 0.5);
                    Hologram hologram = HologramsAPI.createHologram(this, l);
                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', getMainConfig().getString("Messages.Holograms.End")));
                    holograms.add(hologram);
                    l.setY(l.getY() - 2);
                    l.setX(l.getX() - 0.5);
                    l.setZ(l.getZ() - 0.5);
                    PressurePlateInteractListener.setEnd(l);
                } else if (s.equalsIgnoreCase("reset")) {
                    continue;
                } else {
                    Location l = (Location) getStorage().get(s + ".location");
                    l.setY(l.getY() + 2);
                    l.setX(l.getX() + 0.5);
                    l.setZ(l.getZ() + 0.5);
                    Hologram hologram = HologramsAPI.createHologram(this, l);
                    TextLine textLine = hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', getMainConfig().getString("Messages.Holograms.Checkpoint").replace("{checkpoint}",s)));
                    holograms.add(hologram);
                    l.setY(l.getY() - 2);
                    l.setX(l.getX() - 0.5);
                    l.setZ(l.getZ() - 0.5);
                    PressurePlateInteractListener.getCheckLocations().add(l);
                }
            }
        }

    }

}
