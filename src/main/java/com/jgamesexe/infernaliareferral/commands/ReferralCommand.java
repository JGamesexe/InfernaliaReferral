package com.jgamesexe.infernaliareferral.commands;

import com.jgamesexe.infernaliareferral.Config;
import com.jgamesexe.infernaliareferral.Lang;
import com.jgamesexe.infernaliareferral.players.ReferralPlayer;
import com.jgamesexe.infernaliareferral.players.ReferralPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ReferralCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.CMD_ONLY_FOR_PLAYERS);
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Lang.CMD_REFERRAL_HELP);
            return false;
        }

        if (args[0].equalsIgnoreCase("top")) {
            topCommand(player);
            return true;
        }

        if (ReferralPlayerManager.alreadyReferral(player)) {
            player.sendMessage(Lang.CMD_REFERRAL_ALREADY_REFERRAL);
            return false;
        }

        if (player.getLevel() > Config.referralLevelCap) {
            player.sendMessage(Lang.CMD_REFERRAL_LEVEL_CAP);
            return false;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            Lang.sendMessage(player, Lang.CMD_REFERRAL_PLAYER_NOT_FOUND, new String[]{"%player%", args[0]});
            return false;
        } else if (target.getUniqueId() == player.getUniqueId()) {
            player.sendMessage(Lang.CMD_REFERRAL_CANT_REFERRAL_YOURSELF);
            return false;
        }

        ReferralPlayerManager.referral(player, target.getUniqueId());

        Lang.sendMessage(player, Lang.CMD_REFERRAL_DO_REFERRAL, new String[]{"%player%", target.getName()});
        if (Config.doReferralAward.size() > 0)
            for (ItemStack i : Config.doReferralAward)
                if (player.getInventory().firstEmpty() != -1) player.getInventory().addItem(i);
                else player.getWorld().dropItem(player.getLocation(), i);

        Lang.sendMessage(target, Lang.CMD_REFERRAL_RECEIVE_REFERRAL, new String[]{"%player%", player.getName()});
        if (Config.receiveReferralAward.size() > 0)
            for (ItemStack i : Config.receiveReferralAward)
                if (target.getInventory().firstEmpty() != -1) target.getInventory().addItem(i);
                else target.getWorld().dropItem(target.getLocation(), i);

        return true;
    }

    public void topCommand(CommandSender sender) {

        sender.sendMessage(Lang.CMD_REFERRAL_TOP_HEADER);

        for (int index = 1; index <= Config.howManyReferralTops; index++) {
            ReferralPlayer refPlayer = ReferralPlayerManager.getTops().size() >= index ? ReferralPlayerManager.getTops().get(index - 1) : null;
            String playerName = refPlayer == null ? "Ninguém" : Bukkit.getOfflinePlayer(refPlayer.getUuid()).getName();
            if (playerName == null) playerName = "???????";
            Lang.sendMessage(sender, Lang.CMD_REFERRAL_TOP_SHOW,
                    new String[]{"%player%", playerName},
                    new String[]{"%rankcolor%", Config.referralRankColors.containsKey(index) ? Config.referralRankColors.get(index).toString() : ""},
                    new String[]{"%rank%", "" + index});
        }

        sender.sendMessage(Lang.CMD_REFERRAL_TOP_BOTTOM);

    }

}
