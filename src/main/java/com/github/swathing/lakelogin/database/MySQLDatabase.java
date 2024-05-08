package com.github.swathing.lakelogin.database;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MySQLDatabase {

    private Connection connection;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://0.0.0.0:3306/login", "root", "");
            connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS players (ipaddress VARCHAR(16) NOT NULL, name VARCHAR(16) NOT NULL, password text NOT NULL, registrationDate DATETIME NOT NULL)"
            ).execute();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        try {
            if(!connection.isClosed()) return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean isRegistered(@NotNull Player p) {
        try(PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM players WHERE name=?")) {
            ps.setString(1, p.getName());
            if (ps.executeQuery().next()) return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void registerPlayer(@NotNull Player p, String password) {
        String sql = "INSERT INTO players (ipaddress, name, password, registrationDate) VALUES (?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getAddress().getHostString());
            ps.setString(2, p.getName());
            ps.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.setString(4, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPlayerInfo(@NotNull Player p, String infoType) {
        try(PreparedStatement ps = getConnection().prepareStatement("SELECT " + infoType + " FROM players WHERE name=?")) {
            ps.setString(1, p.getName());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updatePlayerInfo(@NotNull Player p, String infoType, String newValue) {
        try {
            getConnection().prepareStatement("UPDATE players SET " + infoType + "='" + newValue + "' WHERE name='" + p.getName() + "'").executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
