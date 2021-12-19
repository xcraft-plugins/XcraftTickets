package de.xcraft.voronwe.XcraftTickets.Commands.Mod;

import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.Commands.XcraftCommand;
import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftCommandManager;
import de.xcraft.voronwe.XcraftTickets.Manager.XcraftPluginManager;
import java.util.Date;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand extends XcraftCommand {
   public WarpCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
      super(cManager, command, name, pattern, usage, desc, permission);
   }

   public boolean execute(XcraftPluginManager pManager, CommandSender sender, String[] args) {
      TicketManager manager = (TicketManager)pManager;
      int id = Integer.parseInt(args[0]);
      Ticket ticket = manager.getTicket(id);
      if (ticket == null) {
         pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_TICKET_NOT_FOUND.toString(Msg.Replace.ID(id)), true);
         return true;
      } else {
         Location loc = ticket.getLoc();
         if (loc.getWorld() == null) {
            loc.setWorld(manager.getPlugin().getServer().createWorld(new WorldCreator(ticket.getWorld())));
         }

         if (sender instanceof Player) {
            Player player = (Player)sender;
            if (player != null) {
               player.teleport(loc);
               player.performCommand("ticket view " + ticket.getId() + " all");
               manager.inform(ticket, Msg.TICKET_BROADCAST_WARP.toString(Msg.Replace.NAME(player.getName()), Msg.Replace.ID(id)), true);
               if (ticket.getAssignee() == null) {
                  ticket.setAssignee(player.getName());
               }

               manager.setLastTicket(sender, ticket.getId());
               ticket.setProcessed((new Date()).getTime());
               player.setNoDamageTicks(200);
            }
         } else {
            pManager.plugin.getMessenger().sendInfo(sender, Msg.ERR_NOT_FROM_CONSOLE.toString(), true);
         }

         return true;
      }
   }
}
