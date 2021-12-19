package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.Log;
import de.xcraft.voronwe.XcraftTickets.Msg;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.XcraftTickets;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketManager extends XcraftPluginManager {
   public ConfigManager cManager = null;
   private int nextID;
   private List<Ticket> tickets = new ArrayList();
   private Map<String, String> phrases = new HashMap();
   private List<String> assignees = new ArrayList();
   private final Map<CommandSender, Integer> lastTicket = new HashMap();
   private final SimpleDateFormat date = new SimpleDateFormat();

   public TicketManager(XcraftTickets plugin) {
      super(plugin);
      this.date.applyPattern("dd.MM HH:mm");
   }

   public List<Ticket> getTickets() {
      return this.tickets;
   }

   public Ticket getTicket(int id) {
      Iterator var2 = this.tickets.iterator();

      Ticket ticket;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         ticket = (Ticket)var2.next();
      } while(ticket.getId() != id);

      return ticket;
   }

   public void setTickets(List<Ticket> tickets) {
      this.tickets = tickets;
   }

   public void onJoinInform(Player player) {
      if (player.hasPermission("XcraftTickets.Mod")) {
         int x = 0;
         Iterator var3 = this.tickets.iterator();

         while(true) {
            Ticket ticket;
            do {
               do {
                  if (!var3.hasNext()) {
                     if (x > 0) {
                        this.plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_UNREAD_LIST.toString(Msg.Replace.MISC(String.valueOf(x))), true);
                     }

                     return;
                  }

                  ticket = (Ticket)var3.next();
               } while(ticket.hasWatched(player.getName()));
            } while(ticket.isAssigned() && !ticket.isAssignee(player, this));

            ++x;
         }
      } else {
         Iterator var5 = this.tickets.iterator();

         while(var5.hasNext()) {
            Ticket ticket = (Ticket)var5.next();
            if (ticket.getOwner().equals(player.getName()) && !ticket.hasWatched(player.getName())) {
               this.plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_UNREAD.toString(Msg.Replace.ID(ticket.getId())), true);
            }
         }

      }
   }

   public void informPlayers(Server server) {
      Map<Player, Integer> mods = new HashMap();
      Iterator var3 = server.getOnlinePlayers().iterator();

      Player player;
      while(var3.hasNext()) {
         player = (Player)var3.next();
         if (player.hasPermission(this.plugin.getDescription().getName() + ".Mod")) {
            mods.put(player, 0);
         }
      }

      var3 = this.tickets.iterator();

      while(true) {
         OfflinePlayer owner;
         Ticket ticket;
         do {
            do {
               if (!var3.hasNext()) {
                  var3 = mods.keySet().iterator();

                  while(var3.hasNext()) {
                     player = (Player)var3.next();
                     if ((Integer)mods.get(player) > 0) {
                        this.plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_UNREAD_LIST.toString(Msg.Replace.MISC(String.valueOf(mods.get(player)))), true);
                     }
                  }

                  var3 = server.getOnlinePlayers().iterator();

                  while(var3.hasNext()) {
                     player = (Player)var3.next();
                     List<String> list = this.cManager.getReminder(player.getName());
                     if (list != null) {
                        if (list.size() > 1) {
                           this.plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_CLOSE_LIST.toString(Msg.Replace.ID(list.size()), Msg.Replace.MISC(list.toString())), true);
                        } else {
                           this.plugin.getMessenger().sendInfo(player, Msg.TICKET_REMIND_CLOSE.toString(Msg.Replace.ID(Integer.parseInt((String)list.get(0)))), true);
                        }
                     }
                  }

                  return;
               }

               ticket = (Ticket)var3.next();
               owner = server.getOfflinePlayer(ticket.getOwner());
            } while(!owner.isOnline());
         } while(ticket.hasWatched(ticket.getOwner()));

         Iterator var6 = mods.keySet().iterator();

         while(var6.hasNext()) {
            Player mod = (Player)var6.next();
            if (!ticket.hasWatched(mod.getName())) {
               mods.put(mod, (Integer)mods.get(mod) + 1);
            }
         }
      }
   }

   public void inform(Ticket ticket, String message, boolean sendToOwner) {
      Player owner = this.plugin.getServer().getPlayer(ticket.getOwner());
      if (owner != null) {
         this.plugin.getMessenger().sendInfo(owner, message, false);
      }

      Iterator var5 = this.plugin.getServer().getOnlinePlayers().iterator();

      while(var5.hasNext()) {
         Player mod = (Player)var5.next();
         if (mod.hasPermission("XcraftTickets.Mod") && !mod.equals(owner)) {
            this.plugin.getMessenger().sendInfo(mod, message, false);
         }
      }

   }

   public String getMessage(CommandSender sender, String[] args) {
      String message = " ";
      int start = 0;
      if (args[0].matches("\\d.*")) {
         start = 1;
      }

      for(int i = start; i < args.length; ++i) {
         message = message + args[i] + " ";
      }

      if (sender.hasPermission("XcraftTickets.Phrases")) {
         Iterator var7 = this.getPhrases().keySet().iterator();

         String s;
         while(var7.hasNext()) {
            s = (String)var7.next();
            if (message.contains(" " + s + " ")) {
               message = message.replace(s, "$[" + s + "]$");
            }
         }

         message = message.trim();
         var7 = this.getPhrases().keySet().iterator();

         while(var7.hasNext()) {
            s = (String)var7.next();
            if (message.contains("$[" + s + "]$")) {
               message = message.replace("$[" + s + "]$", (CharSequence)this.getPhrases().get(s));
            }
         }
      }

      return message;
   }

   public Ticket addTicket(String owner, Location loc, String message) {
      Log log = new Log(this.date);
      log.add(Log.EntryType.OPEN, owner, message);
      Ticket ticket = new Ticket(this.nextID, owner, loc, log);
      this.tickets.add(ticket);
      this.cManager.saveTicket(this.cManager.ticketFolder, ticket);
      ++this.nextID;
      this.cManager.save(ticket);
      return ticket;
   }

   public void addTicket(Ticket ticket) {
      this.tickets.add(ticket);
   }

   public Ticket getArchivedTicket(int id) {
      File archive = new File(this.plugin.getDataFolder(), "/archive/" + id + ".yml");
      return !archive.exists() ? null : this.cManager.loadTicket(archive);
   }

   public void setTicketArchived(Ticket ticket) {
      this.tickets.remove(ticket);
      this.cManager.archiveTicket(ticket);
   }

   public void deleteTicket(Ticket ticket) {
      this.tickets.remove(ticket);
      this.cManager.deleteTicket(ticket);
   }

   public String getCurrentDate() {
      return this.date.format(new Date());
   }

   public int getNextID() {
      return this.nextID;
   }

   public void setNextID(int nextID) {
      this.nextID = nextID;
   }

   public Map<String, String> getPhrases() {
      return this.phrases;
   }

   public void setPhrases(Map<String, String> phrases) {
      this.phrases = phrases;
   }

   public List<String> getAssignees() {
      return this.assignees;
   }

   public void setAssignees(List<String> assignees) {
      this.assignees = assignees;
   }

   public int getLastTicket(CommandSender sender) {
      return this.lastTicket.containsKey(sender) ? (Integer)this.lastTicket.get(sender) : -1;
   }

   public void setLastTicket(CommandSender sender, int id) {
      this.lastTicket.put(sender, id);
   }

   public SimpleDateFormat getDate() {
      return this.date;
   }

   public XcraftTickets getPlugin() {
      return (XcraftTickets)this.plugin;
   }
}
