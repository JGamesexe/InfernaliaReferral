package com.jgamesexe.infernaliareferral;

import org.bukkit.command.CommandSender;

public class Lang {

    public static String CMD_ONLY_FOR_PLAYERS = "Only for players";
    public static String CMD_REFERRAL_HELP = "/referral [targetPlayer]\n/referral top";
    public static String CMD_REFERRAL_PLAYER_NOT_FOUND = "Player %player% not found";
    public static String CMD_REFERRAL_CANT_REFERRAL_YOURSELF = "You cant referral yourself";
    public static String CMD_REFERRAL_LEVEL_CAP = "You are high level to referral someone";
    public static String CMD_REFERRAL_ALREADY_REFERRAL = "You already referral someone";
    public static String CMD_REFERRAL_DO_REFERRAL = "Your reference is %player%, thanks for playing with us";
    public static String CMD_REFERRAL_RECEIVE_REFERRAL = "%player% reference you, thanks for bring new players";
    public static String CMD_REFERRAL_TOP_HEADER = "=- Top Header";
    public static String CMD_REFERRAL_TOP_SHOW = " %rankcolor% %rank%º, %player%";
    public static String CMD_REFERRAL_TOP_BOTTOM = "=- Top Bottom";

    public static void sendMessage(CommandSender to, String message, String[]... replaceTo) {

        for (String[] replacement : replaceTo)
            message = message.replace(replacement[0], replacement[1]);

        to.sendMessage(message);

    }

    public static void loadLang() {
        //TODO???
    }

}
