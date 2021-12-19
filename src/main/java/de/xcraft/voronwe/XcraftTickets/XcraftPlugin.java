package de.xcraft.voronwe.XcraftTickets;

import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftConfigManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftEventListener;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import de.xcraft.voronwe.XcraftTickets.Message.Messenger;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class XcraftPlugin extends JavaPlugin {
   public abstract XcraftPluginManager getPluginManager();

   public abstract XcraftConfigManager getConfigManager();

   public abstract XcraftCommandManager getCommandManager();

   public abstract XcraftEventListener getEventListener();

   public abstract Messenger getMessenger();
}
