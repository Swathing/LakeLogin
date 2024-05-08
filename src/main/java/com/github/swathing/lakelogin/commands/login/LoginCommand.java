package com.github.swathing.lakelogin.commands.login;

import com.github.swathing.lakelogin.Main;
import com.github.swathing.lakelogin.commands.Commands;
import com.github.swathing.lakelogin.listeners.Listeners;
import com.github.swathing.lakelogin.player.LoginProfile;
import com.github.swathing.lakelogin.utils.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.github.swathing.lakelogin.Main.getInstance;

public class LoginCommand extends Commands {

    public LoginCommand() {
        super("login", "logar");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (!Listeners.UNLOGGED.contains(p)) {
                p.sendMessage("§cVocê já está logado!");
                return;
            }

            if(!getInstance().getDatabase().isRegistered(p)) {
                p.sendMessage("§cVocê ainda não está registrado! Utilize /register <senha> <confirmar-senha> para se registrar no servidor.");
                return;
            }

            if(args.length == 0) {
                p.sendMessage("§cUso correto do comando: /login <senha>");
                return;
            }

            LoginProfile profile = new LoginProfile(p);
            if(profile.isPasswordCorrect(args[0])) {
                EnumSound.LEVEL_UP.play(p, 0.5F, 1.0F);
                p.sendMessage("§aLogin concluído! Bem-vindo ao servidor!");
                p.sendTitle(" ", " ");
                getInstance().getDatabase().updatePlayerInfo(p, "ipaddress", p.getAddress().getHostString());
                profile.login();
            } else {
                EnumSound.VILLAGER_NO.play(p, 0.5F, 1.0F);
                p.sendMessage("§cSua senha está incorreta!");
            }
        } else {
            sender.sendMessage("§cApenas jogadores podem utilizar esse comando.");
        }
    }
}
