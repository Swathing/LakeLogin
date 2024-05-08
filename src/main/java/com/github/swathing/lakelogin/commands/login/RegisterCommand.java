package com.github.swathing.lakelogin.commands.login;

import com.github.swathing.lakelogin.commands.Commands;
import com.github.swathing.lakelogin.listeners.Listeners;
import com.github.swathing.lakelogin.utils.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.github.swathing.lakelogin.Main.getInstance;

public class RegisterCommand extends Commands {

    public RegisterCommand() {
        super("register", "registrar");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (!Listeners.UNLOGGED.contains(p)) {
                p.sendMessage("§cVocê já está logado!");
                return;
            }

            if(getInstance().getDatabase().isRegistered(p)) {
                p.sendMessage("§cVocê já está registrado! Utilize /login <senha> para logar no servidor.");
                return;
            }

            if(args.length == 0) {
                p.sendMessage("§cUso correto do comando: /register <senha> <confirmar-senha>");
                return;
            }

            if(args.length == 1) {
                p.sendMessage("§cVocê esqueceu de confirmar a senha! Tente novamente.");
                EnumSound.VILLAGER_NO.play(p, 0.5F, 1.0F);
                return;
            }

            if(args[1].length() < 8) {
                p.sendMessage("§cSua senha precisa ter pelo menos 8 caracteres.");
                EnumSound.VILLAGER_NO.play(p, 0.5F, 1.0F);
                return;
            }

            getInstance().getDatabase().registerPlayer(p, args[1]);
            Listeners.UNLOGGED.remove(p);

            EnumSound.ANVIL_USE.play(p, 0.5F, 1.0F);
            p.sendMessage("§aVocê foi registrado com sucesso! Bem-vindo ao servidor!");
            p.sendTitle(" ", " ");
        } else {
            sender.sendMessage("§cApenas jogadores podem utilizar esse comando.");
        }
    }
}
