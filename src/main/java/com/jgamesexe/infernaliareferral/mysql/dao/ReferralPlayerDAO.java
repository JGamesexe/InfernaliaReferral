package com.jgamesexe.infernaliareferral.mysql.dao;

import com.jgamesexe.infernaliareferral.Config;
import com.jgamesexe.infernaliareferral.Main;
import com.jgamesexe.infernaliareferral.players.ReferralPlayer;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ReferralPlayerDAO {

    public HashMap<UUID, ReferralPlayer> cache = new HashMap<>();
    private HashMap<UUID, Boolean> smoothCache = new HashMap<>();

    public ReferralPlayer get(UUID uuid) {

        if (cache.containsKey(uuid)) return cache.get(uuid);

        ReferralPlayer referralPlayer = null;
        try (PreparedStatement ps = Main.connection.prepareStatement("SELECT * FROM referrals WHERE uuid=?")) {

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                referralPlayer = new ReferralPlayer(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getInt("referralPoints"),
                        rs.getString("referencePlayer") == null ? null : UUID.fromString(rs.getString("referencePlayer")),
                        rs.getInt("lastLevelReferenced"));

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        cache.put(uuid, referralPlayer);

        return referralPlayer;
    }

    public void insert(ReferralPlayer referralPlayer) {
        try (PreparedStatement ps = Main.connection.prepareStatement("INSERT INTO referrals(uuid, referralPoints, referencePlayer, lastLevelReferenced) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, referralPlayer.getUuid().toString());
            ps.setInt(2, referralPlayer.getReferralPoints());
            ps.setString(3, referralPlayer.getReferencePlayer() == null ? null : referralPlayer.getReferencePlayer().toString());
            ps.setInt(4, referralPlayer.getLastLevelReferenced());

            ps.executeUpdate();

            cache.put(referralPlayer.getUuid(), referralPlayer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ReferralPlayer referralPlayer) {
        try (PreparedStatement ps = Main.connection.prepareStatement("UPDATE referrals SET referralPoints=?, referencePlayer=?, lastLevelReferenced=? WHERE uuid=?")) {

            ps.setInt(1, referralPlayer.getReferralPoints());
            ps.setString(2, referralPlayer.getReferencePlayer() != null ? referralPlayer.getReferencePlayer().toString() : null);
            ps.setInt(3, referralPlayer.getLastLevelReferenced());
            ps.setString(4, referralPlayer.getUuid().toString());

            ps.executeUpdate();

            cache.put(referralPlayer.getUuid(), referralPlayer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logPointInsertion(UUID from, UUID to, int amount) {
        try (PreparedStatement ps = Main.connection.prepareStatement("INSERT INTO referrals_log(whenIt, uuidFrom, uuidTarget, amount) VALUES (?, ?, ?, ?)")) {

            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, from.toString());
            ps.setString(3, to.toString());
            ps.setInt(4, amount);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ReferralPlayer> getTops() {
        List<ReferralPlayer> list = new ArrayList<>();
        try (PreparedStatement ps = Main.connection.prepareStatement("SELECT * FROM infernalia.referrals ORDER BY referralPoints DESC LIMIT ?;")) {

            ps.setInt(1, Config.howManyReferralTops);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(new ReferralPlayer(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getInt("referralPoints"),
                        rs.getString("referencePlayer") == null ? null : UUID.fromString(rs.getString("referencePlayer")),
                        rs.getInt("lastLevelReferenced")));

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public void smoothCache() {
        for (UUID uuid : cache.keySet()) {
            if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) {
                if (!smoothCache.containsKey(uuid)) {
                    smoothCache.put(uuid, false);
                } else if (!smoothCache.get(uuid)) {
                    smoothCache.put(uuid, true);
                } else {
                    smoothCache.remove(uuid);
                    cache.remove(uuid);
                }
            } else {
                smoothCache.remove(uuid);
            }
        }
    }

}
