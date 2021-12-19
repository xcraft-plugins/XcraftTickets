package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.XcraftPlugin;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class XcraftConfigManager {
   protected final XcraftPlugin plugin;
   protected FileConfiguration config;

   public XcraftConfigManager(XcraftPlugin plugin) {
      this.plugin = plugin;
      File check = new File(plugin.getDataFolder(), "config.yml");
      if (!check.exists()) {
         plugin.getMessenger().info("[" + plugin.getName() + "] Creating new config folder");
         plugin.saveDefaultConfig();
      }

      plugin.reloadConfig();
      this.config = plugin.getConfig();
   }

   public abstract void load();

   public abstract void save();
}
