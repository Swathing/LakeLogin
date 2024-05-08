package com.github.swathing.lakelogin;

import com.github.swathing.lakelogin.commands.Commands;
import com.github.swathing.lakelogin.database.MySQLDatabase;
import com.github.swathing.lakelogin.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private final MySQLDatabase sql = new MySQLDatabase();

    private static Main instance;

    @Override
    public void onEnable() {
        setInstance(this);
        getDatabase().connect();
        if(!getDatabase().isConnected()) {
            Bukkit.getConsoleSender().sendMessage("§9§l(LakeLogin)§4 ERRO §cNão foi possível conectar a database!");
            System.exit(0);
            return;
        }
        Bukkit.getConsoleSender().sendMessage("§9§l(LakeLogin)§2 SUCCESS §aConectado ao database!");
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Commands.registerCommands();
    }

    public MySQLDatabase getDatabase() {
        return sql;
    }

    public static Main getInstance() {
        return instance;
    }

    public void setInstance(Main instance) {
        Main.instance = instance;
    }
}
