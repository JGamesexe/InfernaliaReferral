package com.jgamesexe.infernaliareferral.players;

import java.util.UUID;

public class ReferralPlayer {

    private UUID uuid;
    private int referralPoints = 0;
    private UUID referencePlayer = null;
    private int lastLevelReferenced = 0;

    public ReferralPlayer(UUID uuid, int referralPoints, UUID referencePlayer, int lastLevelReferenced) {
        this.uuid = uuid;
        this.referralPoints = referralPoints;
        this.referencePlayer = referencePlayer;
        this.lastLevelReferenced = lastLevelReferenced;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getReferralPoints() {
        return referralPoints;
    }

    public void addReferralPoint(int count) {
        this.referralPoints += count;
    }

    public int getLastLevelReferenced() {
        return lastLevelReferenced;
    }

    public void setLastLevelReferenced(int lastLevelReferenced) {
        this.lastLevelReferenced = lastLevelReferenced;
    }

    public UUID getReferencePlayer() {
        return referencePlayer;
    }

    public boolean alreadyReferral() {
        return referencePlayer != null;
    }

    public void setReferencePlayer(UUID referencePlayer) {
        this.referencePlayer = referencePlayer;
    }
}
