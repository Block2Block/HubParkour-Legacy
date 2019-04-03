package io.github.Block2Block.HubParkour.MySQL;

import io.github.Block2Block.HubParkour.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;


public class DatabaseManager {

    public static DatabaseManager i;

    private static MySQL dbMySql;
    private static SQLite dbSqlite;
    private static boolean isMysql;
    private static Connection connection;

    private static boolean error;

    public DatabaseManager(boolean isMysql) {
        i = this;
    }

    public void setup(boolean isMySql) throws SQLException, ClassNotFoundException {
        this.isMysql = isMySql;
        if (isMysql) {
            dbMySql = new MySQL(Main.getMainConfig().getString("Settings.Database.Details.MySQL.Hostname"), Main.getMainConfig().getString("Settings.Database.Details.MySQL.Port"),Main.getMainConfig().getString("Settings.Database.Details.MySQL.Database"), Main.getMainConfig().getString("Settings.Database.Details.MySQL.Username"), Main.getMainConfig().getString("Settings.Database.Details.MySQL.Password"));
            connection = dbMySql.openConnection();
        } else {
            dbSqlite = new SQLite(Main.getMainConfig().getString("Settings.Database.Details.SQLite.File-Name"));
            connection = dbSqlite.openConnection();
        }
        createTables();

    }

    private void createTables() {
            try {
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ? (`uuid` varchar(32), `time` bigint(64), `name` varchar(16))");
                statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes"));
                boolean set = statement.execute();

                statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ? (`type` bit(3),`x` bigint(64),`y` bigint(64),`z` bigint(64), `checkno` bit(64) null default null, `world` varchar(64))");
                statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.Locations"));
                set = statement.execute();
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "There has been an error creating the tables. Database functionality has been disabled until the server is restarted. Try checking your config file to ensure that all details are correct and that your database is online. Stack trace:");
                error = true;
                e.printStackTrace();
            }
    }

    public void addLocation(Location location, int type, int checkNo) {
        if (error) return;
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ? VALUES (?,?,?,?,?,?)");
            statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.Locations"));
            statement.setInt(2, type);
            statement.setInt(3, x);
            statement.setInt(4, y);
            statement.setInt(5, z);
            statement.setString(6, location.getWorld().getName());

            if (checkNo == -1) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setInt(6, checkNo);
            }

            statement.execute();

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "There has been an error creating the tables. Database functionality has been disabled until the server is restarted. Try checking your config file to ensure that all details are correct and that your database is online. Stack trace:");
            error = true;
            e.printStackTrace();
        }

    }

    public void setLocation(Location location, int type, int checkNo) {
        if (error) return;
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        try {
            PreparedStatement statement;

            if (type==3) {
                statement = connection.prepareStatement("UPDATE ? SET x = ?,y = ?, z = ?, world = ? WHERE type = ?");

                statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.Locations"));
                statement.setInt(2, x);
                statement.setInt(3, y);
                statement.setInt(4, z);
                statement.setString(5, location.getWorld().getName());
                statement.setInt(6, type);
            } else {
                statement = connection.prepareStatement("UPDATE ? SET x = ?,y = ?, z = ?, world = ? WHERE checkno = ?");

                statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.Locations"));
                statement.setInt(2, x);
                statement.setInt(3, y);
                statement.setInt(4, z);
                statement.setString(5, location.getWorld().getName());
                statement.setInt(6, checkNo);
            }

            statement.execute();

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "There has been an error creating the tables. Database functionality has been disabled until the server is restarted. Try checking your config file to ensure that all details are correct and that your database is online. Stack trace:");
            error = true;
            e.printStackTrace();
        }

    }

    public HashMap<Integer, List<String>> getLeaderboard() {
        HashMap<Integer, List<String>> leaderboard = new HashMap<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ? ORDER BY `time` ASC ");
            statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes"));


            ResultSet results = statement.executeQuery();
            if (results.next()) {
                List<String> record = new ArrayList<>();
                record.add(results.getString(3));
                record.add(results.getString(2));
                leaderboard.put(1, record);
            }
            if (results.next()) {
                List<String> record = new ArrayList<>();
                record.add(results.getString(3));
                record.add(results.getString(2));
                leaderboard.put(2, record);
            }
            if (results.next()) {
                List<String> record = new ArrayList<>();
                record.add(results.getString(3));
                record.add(results.getString(2));
                leaderboard.put(3, record);
            }


        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "There has been an error accessing the database. Database functionality has been disabled until the server is restarted. Try checking your database is online. Stack trace:");
            error = true;
            e.printStackTrace();
        }
        return leaderboard;
    }

    public List<List<String>> getLocations() {
        List<List<String>> locations = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ? ");
            statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.Locations"));

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                List<String> location = new ArrayList<>();
                location.add(results.getString(1));
                location.add(results.getString(2));
                location.add(results.getString(3));
                location.add(results.getString(4));
                location.add(results.getString(5));

                locations.add(location);
            }

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "There has been an error accessing the database. Database functionality has been disabled until the server is restarted. Try checking your database is online. Stack trace:");
            error = true;
            e.printStackTrace();
        }

        return locations;
    }

    public boolean beatBefore(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ? WHERE uuid = ?");
            statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes"));
            statement.setString(2, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "There has been an error accessing the database. Database functionality has been disabled until the server is restarted. Try checking your database is online. Stack trace:");
            error = true;
            e.printStackTrace();
        }
        return false;
    }

    public boolean beatOldRecord(Player player, long time) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT time FROM ? WHERE uuid = ?");
            statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes"));
            statement.setString(2, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            if (resultSet.getLong(1) < time) {
                return true;
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "There has been an error accessing the database. Database functionality has been disabled until the server is restarted. Try checking your database is online. Stack trace:");
            error = true;
            e.printStackTrace();
        }
        return false;
    }

    public void newTime(Player player, long time, boolean beatBefore) {
        if (beatBefore) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE ? SET time = ? WHERE uuid = ?");

                statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes"));
                statement.setLong(2, time);
                statement.setString(3, player.getUniqueId().toString());

                ResultSet resultSet = statement.executeQuery();
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "There has been an error accessing the database. Database functionality has been disabled until the server is restarted. Try checking your database is online. Stack trace:");
                error = true;
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO ?(uuid, time, name) values (?,?,?)");

                statement.setString(1, Main.getMainConfig().getString("Settings.Database.Tables.PlayerTimes"));
                statement.setString(2, player.getUniqueId().toString());
                statement.setLong(3, time);
                statement.setString(4, player.getName());

                ResultSet resultSet = statement.executeQuery();
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "There has been an error accessing the database. Database functionality has been disabled until the server is restarted. Try checking your database is online. Stack trace:");
                error = true;
                e.printStackTrace();
            }

        }
    }


}
