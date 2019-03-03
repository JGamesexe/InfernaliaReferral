package com.jgamesexe.infernaliareferral;

import com.jgamesexe.infernaliareferral.commands.ReferralCommand;
import com.jgamesexe.infernaliareferral.events.PlayerEvents;
import com.jgamesexe.infernaliareferral.mysql.ConnectionFactory;
import com.jgamesexe.infernaliareferral.players.ReferralPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    public static Connection connection;

    @Override
    public void onEnable() {

        plugin = this;
        saveDefaultConfig();

        Config.loadConfig();
        if (!Config.enable) {
            log("§cPlugin isn't enable...");
            return;
        }

        ConnectionFactory.initConnection();
        if (connection == null) {
            log("§cCan't stabilize connection, plugin disable.");
            return;
        }

        Lang.loadLang();

        Bukkit.getPluginCommand("referral").setExecutor(new ReferralCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);

        ReferralPlayerManager.init();

    }

    @Override
    public void onDisable() {

        if (!Config.enable) {
            log("§cPlugin isn't enable...");
            return;
        }

        ConnectionFactory.closeConnection(connection);

    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage("§7[§cInfernalia§3Referral§7] §f" + msg);
    }


}
