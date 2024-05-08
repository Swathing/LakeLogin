package com.github.swathing.lakelogin.player;

import com.github.swathing.lakelogin.listeners.Listeners;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

import static com.github.swathing.lakelogin.Main.getInstance;

public class LoginProfile {

    public static Set<LoginProfile> PROFILES = new HashSet<>();

    private final Player player;
    private final String password;
    private final String ipAddress;

    public LoginProfile(Player player) {
        this.player = player;
        this.password = getInstance().getDatabase().getPlayerInfo(player, "password");
        this.ipAddress = getInstance().getDatabase().getPlayerInfo(player, "ipaddress");
        PROFILES.add(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void login() {
        Listeners.UNLOGGED.remove(getPlayer());
    }

    public boolean isPasswordCorrect(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
