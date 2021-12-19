package de.xcraft.voronwe.XcraftTickets;

import de.xcraft.voronwe.XcraftTickets.Manager.TicketManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ticket {
   private int id;
   private String owner;
   private String assignee;
   private Location loc;
   private String world;
   private long processed;
   private List<String> watched = new ArrayList();
   private Log log;

   public Ticket(int id, String assignee, Location loc, String world, long processed, List<String> watched, Log log) {
      this.id = id;
      this.owner = log.getEntry(0).player;
      this.assignee = assignee;
      this.loc = loc;
      this.world = world;
      this.processed = processed;
      this.watched = watched;
      this.log = log;
   }

   public Ticket(int id, String owner, Location loc, Log log) {
      this.id = id;
      this.owner = owner;
      this.loc = loc;
      this.world = loc.getWorld().getName();
      this.processed = (new Date()).getTime();
      this.watched = new ArrayList();
      this.log = log;
   }

   public boolean hasWatched(String player) {
      return this.watched.contains(player);
   }

   public void clearWatched() {
      this.watched.clear();
   }

   public boolean addToWatched(String player) {
      if (!this.watched.contains(player)) {
         this.watched.add(player);
         return true;
      } else {
         return false;
      }
   }

   public boolean isAssignee(CommandSender player, TicketManager manager) {
      return this.assignee != null && (this.assignee.equals(player.getName()) || manager.getPlugin().getPermission().playerInGroup((Player)player, this.assignee));
   }

   public boolean isAssigned() {
      return this.assignee != null;
   }

   public List<String> getWatched() {
      return this.watched;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getOwner() {
      return this.owner;
   }

   public void setOwner(String owner) {
      this.owner = owner;
   }

   public String getAssignee() {
      return this.assignee;
   }

   public void setAssignee(String assignee) {
      this.assignee = assignee;
   }

   public Location getLoc() {
      return this.loc;
   }

   public void setLoc(Location loc) {
      this.loc = loc;
   }

   public String getWorld() {
      return this.world;
   }

   public void setWorld(String world) {
      this.world = world;
   }

   public long getProcessed() {
      return this.processed;
   }

   public void setProcessed(long processed) {
      this.processed = processed;
   }

   public Log getLog() {
      return this.log;
   }

   public void setLog(Log log) {
      this.log = log;
   }
}
