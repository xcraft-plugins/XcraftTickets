package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.XcraftPlugin;

public abstract class XcraftPluginManager {
   public XcraftPlugin plugin;

   public XcraftPluginManager(XcraftPlugin plugin) {
      this.plugin = plugin;
   }

   public abstract XcraftPlugin getPlugin();
}
