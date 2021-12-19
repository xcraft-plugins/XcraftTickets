package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.XcraftPlugin;
import org.bukkit.event.Listener;

public abstract class XcraftEventListener implements Listener {
   protected XcraftPlugin plugin;

   public XcraftEventListener(XcraftPlugin plugin) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }
}
