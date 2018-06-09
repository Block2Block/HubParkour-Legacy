package io.github.Block2Block.HubParkour;

import com.avaje.ebean.validation.NotNull;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import io.github.Block2Block.HubParkour.Commands.CommandParkour;
import io.github.Block2Block.HubParkour.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Override
    public void onEnable() {
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
        loadConfigs();
        registerListeners(new LeaveListener(), new PlayerToggleFlyListener(), new PressurePlateInteractListener(), new JoinListener());
        getCommand("parkour").setExecutor(new CommandParkour());
        getLogger().info("HubParkour has been successfully enabled.");
        if (!useHolographicDisplays) {
            getLogger().info("HolographicDisplays not found.");
        } else {
            getLogger().info("HolographicDisplays has been detected.");
            if (getMainConfig().getBoolean("Settings.Holograms")) {
                if (getStorage().isSet("spawn.location")&&getStorage().isSet("end.location")&&getStorage().isSet("reset.location")) {
                    generateHolograms();
                } else {
                    getLogger().info("The parkour has not been setup yet, the Holograms have not been generated.");
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
            if (getMainConfig().getBoolean("Settings.Version-Checker")) {
                if (newVersionCheck()) {
                    getLogger().info("There is a new version out! I highly recommend you download the new version!");
                }
            }
        }
    }

    public boolean newVersionCheck() {
        try {
            HttpURLConnection c = (HttpURLConnection)new URL("http://www.spigotmc.org/api/general.php").openConnection();
            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=47713").getBytes("UTF-8"));
            String oldVersion = this.getDescription().getVersion();
            String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine().replaceAll("[a-zA-Z ]", "");
            if(!newVersion.equals(oldVersion)) {
                return true;
            }
            return false;
        }
        catch(Exception e) {
            getLogger().info("Unable to check for new versions.");
        }
        return false;
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



    private void generateHolograms() {
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
