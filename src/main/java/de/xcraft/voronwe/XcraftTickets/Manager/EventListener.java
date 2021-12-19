package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.XcraftTickets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener extends XcraftEventListener {
   private TicketManager tManager;

   public EventListener(XcraftTickets plugin) {
      super(plugin);
      this.tManager = plugin.getPluginManager();
   }

   @EventHandler(
      priority = EventPriority.LOW
   )
   public void onPlayerJoin(PlayerJoinEvent event) {
      this.tManager.onJoinInform(event.getPlayer());
   }
}
