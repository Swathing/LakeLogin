package com.github.swathing.lakelogin.commands;

import com.github.swathing.lakelogin.commands.login.LoginCommand;
import com.github.swathing.lakelogin.commands.login.RegisterCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public abstract class Commands extends Command {

    public Commands(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));

        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            simpleCommandMap.register(this.getName(), "lakelogin", this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void perform(CommandSender commandSender, String[] args);

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        this.perform(commandSender, strings);
        return true;
    }

    public static void registerCommands() {
        new LoginCommand();
        new RegisterCommand();
    }
}
