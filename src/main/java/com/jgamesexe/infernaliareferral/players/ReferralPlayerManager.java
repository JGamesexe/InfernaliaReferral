package com.jgamesexe.infernaliareferral.players;

import com.jgamesexe.infernaliareferral.Config;
import com.jgamesexe.infernaliareferral.Main;
import com.jgamesexe.infernaliareferral.mysql.dao.ReferralPlayerDAO;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReferralPlayerManager {

    private static ReferralPlayerDAO dao = new ReferralPlayerDAO();
    private static List<ReferralPlayer> tops = new ArrayList<>();

    public static void referral(Player from, UUID to) {
        int fromValue = getPlayerValue(from);
        if (fromValue == 0) return;

        ReferralPlayer fromRefPlayer = dao.get(from.getUniqueId());
        if (fromRefPlayer == null) {
            fromRefPlayer = new ReferralPlayer(from.getUniqueId(), 0, to, from.getLevel());
            dao.insert(fromRefPlayer);
        } else {
            if (!fromRefPlayer.alreadyReferral()) fromRefPlayer.setReferencePlayer(to);
            fromRefPlayer.setLastLevelReferenced(from.getLevel());
            dao.update(fromRefPlayer);
        }

        ReferralPlayer toRefPlayer = dao.get(to);
        if (toRefPlayer == null) {
            toRefPlayer = new ReferralPlayer(to, fromValue, null, 0);
            dao.insert(toRefPlayer);
        } else {
            toRefPlayer.addReferralPoint(fromValue);
            dao.update(toRefPlayer);
        }

        dao.logPointInsertion(from.getUniqueId(), to, fromValue);
        Main.log(from.getName() + " given " + fromValue + " referralPoints to " + to);

    }

    private static int getPlayerValue(Player player) {
        int amount = 0;
        ReferralPlayer refPlayer = dao.get(player.getUniqueId());

        if (refPlayer == null || !refPlayer.alreadyReferral())
            if (player.getLevel() > Config.referralLevelCap) return 0;
            else amount += Config.referralWeight;

        for (Integer[] levelBonus : Config.referralLevelBonus) {
            if (refPlayer != null && levelBonus[0] <= refPlayer.getLastLevelReferenced()) continue;
            if (player.getLevel() >= levelBonus[0]) amount += levelBonus[1];
        }

        return amount;
    }

    public static boolean alreadyReferral(Player player) {
        ReferralPlayer referralPlayer = dao.get(player.getUniqueId());
        if (referralPlayer == null) return false;
        return referralPlayer.alreadyReferral();
    }

    public static ReferralPlayer get(UUID player) {
        return dao.get(player);
    }

    public static List<ReferralPlayer> getTops() {
        return tops;
    }

    public static void init() {

        tops = dao.getTops();

        new BukkitRunnable() {

            @Override
            public void run() { dao.smoothCache(); }

        }.runTaskTimerAsynchronously(Main.plugin, 20 * 5, 20 * 5);

        new BukkitRunnable() {

            @Override
            public void run() { tops = dao.getTops(); }

        }.runTaskTimerAsynchronously(Main.plugin, 20 * 300, 20 * 300);

    }

}
