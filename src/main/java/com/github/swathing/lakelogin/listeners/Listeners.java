package com.github.swathing.lakelogin.listeners;

import com.github.swathing.lakelogin.player.LoginProfile;
import com.github.swathing.lakelogin.utils.EnumSound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.github.swathing.lakelogin.Main.getInstance;

public class Listeners implements Listener {

    public static Set<Player> UNLOGGED = new HashSet<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        
        //ta feio mas serve sua função
        p.sendMessage(" \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n ");

        UNLOGGED.add(p);
        if(!getInstance().getDatabase().isRegistered(p)) {
            p.sendTitle("§a§lBEM-VINDO!", "§fRegistre-se para continuar");
            EnumSound.ITEM_PICKUP.play(p, 1.0F, 1.0F);
            p.sendMessage(
                    " \n§a§lBEM-VINDO AO SERVIDOR!\n \n" +
                    "§fComo você é novo no servidor, você vai precisar se registrar aqui.\n" +
                    "Utilize o comando §6/register <senha> <confirmar-senha> §fpara se registrar!\n "
            );
            return;
        }

        p.sendTitle("§a§lBEM-VINDO!", "§fFaça login para continuar.");
        EnumSound.ITEM_PICKUP.play(p, 1.0F, 1.0F);
        p.sendMessage(
                " \n§a§lBem-vindo de volta, " + p.getName() + "!\n \n" +
                "§fUse §6/login <senha> §fpara logar no servidor.\n "
        );
        p.teleport(p.getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UNLOGGED.remove(p);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(UNLOGGED.contains(p)) {
            e.setCancelled(true);
        }
        p.sendMessage("§cVocê precisa estar logado para falar no chat!");
    }

    @EventHandler
    public void onCommand(ServerCommandEvent e) {
        if(e.getSender() instanceof Player) {
            if (UNLOGGED.contains((Player) e.getSender()) && !Arrays.asList("login", "logar", "register", "registrar").contains(e.getCommand().toLowerCase())) {
                e.setCancelled(true);
            }
        }
    }
}
