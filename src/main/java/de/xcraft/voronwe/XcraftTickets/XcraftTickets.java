package de.xcraft.voronwe.XcraftTickets;

import de.xcraft.voronwe.XcraftTickets.Manager.CommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.ConfigManager;
import de.xcraft.voronwe.XcraftTickets.Manager.EventListener;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Message.Messenger;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class XcraftTickets extends XcraftPlugin {
   private TicketManager ticketManager = null;
   private ConfigManager configManager = null;
   private CommandManager commandManager = null;
   private EventListener eventListener = null;
   private Messenger messenger = null;
   private Permission permission;

   public void onEnable() {
      this.messenger = new Messenger(this);
      Msg.init(this);
      this.setupPermissions();
      this.ticketManager = new TicketManager(this);
      this.configManager = new ConfigManager(this);
      this.eventListener = new EventListener(this);
      this.commandManager = new CommandManager(this);
      this.configManager.load();
      this.startScheduler();
   }

   public void onDisable() {
      if (this.getConfigManager() != null) {
         this.getConfigManager().save();
      }

   }

   public TicketManager getPluginManager() {
      return this.ticketManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public EventListener getEventListener() {
      return this.eventListener;
   }

   public Messenger getMessenger() {
      return this.messenger;
   }

   public Permission getPermission() {
      return this.permission;
   }

   private Boolean setupPermissions() {
      RegisteredServiceProvider<Permission> permissionProvider = this.getServer().getServicesManager().getRegistration(Permission.class);
      if (permissionProvider != null) {
         this.permission = (Permission)permissionProvider.getProvider();
      }

      return this.permission != null;
   }

   public void startScheduler() {
      SimpleDateFormat d = new SimpleDateFormat();
      d.applyPattern("mm:ss");
      String current = d.format(new Date());
      String[] split = current.split(":");
      int min = Integer.parseInt(split[0]);
      int sec = Integer.parseInt(split[1]);
      int delay = 5;
      min = delay - min % delay - 1;
      if (min == -1) {
         min = delay--;
      }

      sec = 60 - sec;
      Runnable task = new Runnable() {
         public void run() {
            XcraftTickets.this.getPluginManager().informPlayers(XcraftTickets.this.getServer());
         }
      };
      this.getServer().getScheduler().runTaskTimerAsynchronously(this, task, (long)((min * 60 + sec) * 20), (long)(60 * delay * 20));
   }
}
