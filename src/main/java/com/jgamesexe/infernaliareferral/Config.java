package com.jgamesexe.infernaliareferral;

import com.jgamesexe.infernaliareferral.events.PlayerEvents;
import com.jgamesexe.infernaliareferral.utils.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {

    public static boolean enable = false;

    public static String mysqlIP;
    public static String mysqlPort;
    public static String mysqlUser;
    public static String mysqlPass;
    public static String mysqlDatabase;

    public static int howManyReferralTops;
    public static int referralWeight;
    public static int referralLevelCap;
    public static List<Integer[]> referralLevelBonus;
    public static List<ItemStack> doReferralAward;
    public static List<ItemStack> receiveReferralAward;
    public static HashMap<Integer, ChatColor> referralRankColors;

    static void loadConfig() {
        try {

            String filePath = Main.plugin.getDataFolder().getAbsolutePath() + "/config.yml";

            ConfigManager configManager = new ConfigManager(filePath);
            FileConfiguration cfg = configManager.getConfig();

            enable = cfg.getBoolean("Enable");
            if (!enable) return;

            mysqlIP = cfg.getString("MySQL.Ip");
            mysqlPort = cfg.getString("MySQL.Port");
            mysqlUser = cfg.getString("MySQL.User");
            mysqlPass = cfg.getString("MySQL.Password");
            mysqlDatabase = cfg.getString("MySQL.Database");

            howManyReferralTops = cfg.getInt("ReferralOptions.HowManyTops");
            referralWeight = cfg.getInt("ReferralOptions.ReferralWeight");
            referralLevelCap = cfg.getInt("ReferralOptions.LevelCap");
            referralLevelBonus = refLevelBonusFromStringList(cfg.getStringList("ReferralOptions.LevelBonus"));

            ArrayList<Integer> actionsLevels = new ArrayList<>();
            for (Integer[] x : referralLevelBonus) actionsLevels.add(x[0]);
            PlayerEvents.actionsLevels = actionsLevels;

            doReferralAward = doItemstackListFromStringList(cfg.getStringList("ReferralOptions.DoReferralAward"));
            receiveReferralAward = doItemstackListFromStringList(cfg.getStringList("ReferralOptions.ReceiveReferralAward"));
            referralRankColors = refRankColors(cfg.getStringList("ReferralOptions.RankColors"));

        } catch (Exception e) {
            e.printStackTrace();
            enable = false;
        }
    }

    private static List<Integer[]> refLevelBonusFromStringList(List<String> list) throws Exception {
        List<Integer[]> levelBonus = new ArrayList<>();
        for (String s : list) {
            s = s.replace("[", "");
            s = s.replace("]", "");
            String[] split = s.split(",");

            if (split.length != 2) throw new Exception("Wrong format at ConfigFile ReferralOptions.LevelBonus");
            levelBonus.add(new Integer[]{Integer.valueOf(split[0]), Integer.valueOf(split[1])});
        }

        return levelBonus;
    }

    private static List<ItemStack> doItemstackListFromStringList(List<String> list) throws Exception {
        List<ItemStack> awards = new ArrayList<>();
        try {
            for (String s : list) {
                s = s.replace("[", "");
                s = s.replace("]", "");
                String[] split = s.split(",");

                if (split.length == 2) awards.add(new ItemStack(Material.valueOf(split[0].toUpperCase()), Integer.valueOf(split[1])));
                else throw new Exception("Invalid Format");
            }
        } catch (Exception e) {
            throw new Exception("Error at ConfigFile in some ItemstackList (" + e.getMessage() + ")");
        }

        return awards;
    }

    private static HashMap<Integer, ChatColor> refRankColors(List<String> list) throws Exception {
        HashMap<Integer, ChatColor> rankColors = new HashMap<>();
        for (String s : list) {
            s = s.replace("[", "");
            s = s.replace("]", "");
            String[] split = s.split(",");

            if (split.length != 2) throw new Exception("Wrong format at ConfigFile ReferralOptions.RankColors");
            rankColors.put(Integer.valueOf(split[0]), ChatColor.valueOf(split[1]));
        }

        return rankColors;
    }

}
