package com.jgamesexe.infernaliareferral.events;

import com.jgamesexe.infernaliareferral.players.ReferralPlayer;
import com.jgamesexe.infernaliareferral.players.ReferralPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.ArrayList;

public class PlayerEvents implements Listener {

    public static ArrayList<Integer> actionsLevels;

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent event) {
        if (actionsLevels.size() == 0 || !actionsLevels.contains(event.getPlayer().getLevel())) return;

        ReferralPlayer refPlayer = ReferralPlayerManager.get(event.getPlayer().getUniqueId());
        if (refPlayer == null || !refPlayer.alreadyReferral() || refPlayer.getLastLevelReferenced() >= event.getPlayer().getLevel()) return;

        ReferralPlayerManager.referral(event.getPlayer(), refPlayer.getReferencePlayer());
    }

}
