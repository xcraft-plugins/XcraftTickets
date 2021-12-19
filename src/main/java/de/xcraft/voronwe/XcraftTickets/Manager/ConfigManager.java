package de.xcraft.voronwe.XcraftTickets.Manager;

import de.xcraft.voronwe.XcraftTickets.Log;
import de.xcraft.voronwe.XcraftTickets.Ticket;
import de.xcraft.voronwe.XcraftTickets.XcraftTickets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager extends XcraftConfigManager {
   TicketManager tmanager;
   File ticketFolder;
   File archiveFolder;
   File remFile;
   FileConfiguration reminder;

   public ConfigManager(XcraftTickets plugin) {
      super(plugin);
      this.tmanager = plugin.getPluginManager();
      this.tmanager.cManager = this;
   }

   public void load() {
      File folder = this.plugin.getDataFolder();
      this.ticketFolder = new File(folder, "tickets");
      this.archiveFolder = new File(folder, "archive");
      if (!this.ticketFolder.exists()) {
         this.ticketFolder.mkdir();
      }

      if (!this.archiveFolder.exists()) {
         this.archiveFolder.mkdir();
      }

      this.remFile = new File(folder, "reminder.yml");
      this.reminder = YamlConfiguration.loadConfiguration(this.remFile);
      List<Ticket> tickets = new ArrayList();
      File[] files = this.ticketFolder.listFiles();
      if (files != null) {
         Arrays.sort(files);
         File[] var4 = files;
         int var5 = files.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            Ticket ticket = this.loadTicket(file);
            if (ticket != null) {
               tickets.add(ticket);
            } else {
               file.delete();
            }
         }
      }

      this.tmanager.setTickets(tickets);
      this.tmanager.setNextID(this.config.getInt("Next_Ticket_ID", 1));
      this.tmanager.getDate().applyPattern(this.config.getString("DateFormat", "dd.MM HH:mm"));
      this.tmanager.setAssignees((List<String>) this.config.getList("Assignee"));
      Map<String, String> phrases = new HashMap();
      if (this.config.isConfigurationSection("Phrases")) {
         ConfigurationSection cs = this.config.getConfigurationSection("Phrases");
         Iterator var11 = cs.getKeys(false).iterator();

         while(var11.hasNext()) {
            String key = (String)var11.next();
            phrases.put(key, cs.getString(key));
         }
      }

      this.tmanager.setPhrases(phrases);
   }

   public void save() {
      this.config.set("Next_Ticket_ID", this.tmanager.getNextID());
      this.config.set("Assignee", this.tmanager.getAssignees());
      this.config.set("Phrases", this.tmanager.getPhrases());
      this.plugin.saveConfig();

      try {
         this.reminder.save(this.remFile);
      } catch (IOException var3) {
      }

      Iterator var1 = this.tmanager.getTickets().iterator();

      while(var1.hasNext()) {
         Ticket ticket = (Ticket)var1.next();
         this.saveTicket(this.ticketFolder, ticket);
      }

   }

   public void save(Ticket ticket) {
      this.config.set("Next_Ticket_ID", this.tmanager.getNextID());
      this.config.set("Assignee", this.tmanager.getAssignees());
      this.config.set("Phrases", this.tmanager.getPhrases());
      this.plugin.saveConfig();

      try {
         this.reminder.save(this.remFile);
      } catch (IOException var3) {
      }

      this.saveTicket(this.ticketFolder, ticket);
   }

   public Ticket loadTicket(File ticket) {
      String filename = ticket.getAbsoluteFile().getName().replace(".yml", "");
      if (!filename.matches("\\d*")) {
         return null;
      } else {
         int id = Integer.parseInt(filename);
         FileConfiguration temp = YamlConfiguration.loadConfiguration(ticket);
         ConfigurationSection cs = temp.getConfigurationSection("Ticket");
         Log log = new Log(this.tmanager.getDate());
         List<String> list = (List<String>) cs.getList("log");
         if (list != null && !list.isEmpty()) {
            long processed;
            for(int i = 0; i < list.size(); ++i) {
               String[] split = ((String)list.get(i)).split("; ");
               processed = split[0].matches("\\d*") ? Long.valueOf(split[0]) : 0L;
               log.add(processed, Log.EntryType.valueOf(split[2]), split[1], split.length >= 4 ? split[3] : "");
            }

            List<String> watched = (ArrayList)cs.getList("watched");
            if (watched == null) {
               watched = new ArrayList();
            }

            String assignee = cs.getString("assignee");
            if (assignee != null && assignee.equals("none")) {
               assignee = null;
            }

            processed = 0L;
            if (cs.isLong("processed")) {
               processed = cs.getLong("processed");
            } else {
               processed = (new Date()).getTime();
            }

            cs = temp.getConfigurationSection("Ticket.location");
            Location loc = null;
            String world = null;
            if (cs != null) {
               world = cs.getString("world");
               World w = this.plugin.getServer().getWorld(world);
               loc = new Location(w, (double)cs.getLong("x"), (double)cs.getLong("y"), (double)cs.getLong("z"), (float)cs.getLong("pitch"), (float)cs.getLong("yaw"));
            }

            return new Ticket(id, assignee, loc, world, processed, watched, log);
         } else {
            this.plugin.getMessenger().warning("[" + this.plugin.getName() + "] Ticket " + id + " could not be loaded: missing log!");
            return null;
         }
      }
   }

   public void saveTicket(File folder, Ticket ticket) {
      File file = new File(folder, ticket.getId() + ".yml");
      FileConfiguration temp = YamlConfiguration.loadConfiguration(file);
      temp.set("Ticket.assignee", ticket.getAssignee());
      List<String> list = new ArrayList();

      for(int i = 0; i < ticket.getLog().size(); ++i) {
         list.add(ticket.getLog().getEntry(i).toString());
      }

      temp.set("Ticket.processed", ticket.getProcessed());
      temp.set("Ticket.log", list);
      temp.set("Ticket.watched", ticket.getWatched());
      Location loc = ticket.getLoc();
      if (loc != null && loc.getWorld() != null) {
         temp.set("Ticket.location.x", loc.getX());
         temp.set("Ticket.location.y", loc.getY());
         temp.set("Ticket.location.z", loc.getZ());
         temp.set("Ticket.location.pitch", loc.getPitch());
         temp.set("Ticket.location.yaw", loc.getYaw());
         temp.set("Ticket.location.world", ticket.getWorld());
      }

      try {
         temp.save(file);
      } catch (IOException var8) {
      }

   }

   public void archiveTicket(Ticket ticket) {
      this.saveTicket(this.archiveFolder, ticket);
      this.deleteTicket(ticket);
   }

   public void deleteTicket(Ticket ticket) {
      File file = new File(this.ticketFolder, ticket.getId() + ".yml");
      file.delete();
   }

   public Map<String, Integer> getStats() {
      File statsFile = new File(this.plugin.getDataFolder(), "stats.yml");
      FileConfiguration data = null;
      Map<String, Integer> stats = new HashMap();
      int lastcheck = 0;
      Iterator var6;
      String key;
      if (!statsFile.exists()) {
         try {
            statsFile.createNewFile();
            data = YamlConfiguration.loadConfiguration(statsFile);
         } catch (IOException var18) {
            var18.printStackTrace();
         }
      } else {
         data = YamlConfiguration.loadConfiguration(statsFile);
         if (data.isConfigurationSection("Stats")) {
            ConfigurationSection cs = data.getConfigurationSection("Stats");
            var6 = cs.getKeys(false).iterator();

            while(var6.hasNext()) {
               key = (String)var6.next();
               stats.put(key, cs.getInt(key));
            }
         }

         lastcheck = data.getInt("LastTicket");
      }

      int newcheck = lastcheck;
      File[] var21 = this.archiveFolder.listFiles();
      int var22 = var21.length;

      for(int var8 = 0; var8 < var22; ++var8) {
         File file = var21[var8];
         int number = Integer.parseInt(file.getName().replace(".yml", ""));
         if (number > lastcheck) {
            if (number > newcheck) {
               newcheck = number;
            }

            try {
               FileInputStream fis = new FileInputStream(file);
               InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
               BufferedReader br = new BufferedReader(isr);
               br.mark(1);
               int bom = br.read();
               if (bom != 65279) {
                  br.reset();
               }

               String s;
               while((s = br.readLine()) != null) {
                  if (s.contains("CLOSE")) {
                     String[] split = s.split("; ");
                     if (stats.containsKey(split[1])) {
                        stats.put(split[1], (Integer)stats.get(split[1]) + 1);
                     } else {
                        stats.put(split[1], 1);
                     }
                     break;
                  }
               }

               br.close();
            } catch (IOException var19) {
               var19.printStackTrace();
            }
         }
      }

      data.set("LastTicket", newcheck);
      var6 = stats.keySet().iterator();

      while(var6.hasNext()) {
         key = (String)var6.next();
         data.set("Stats." + key, stats.get(key));
      }

      try {
         data.save(statsFile);
      } catch (IOException var17) {
         var17.printStackTrace();
      }

      return stats;
   }

   public void addReminder(String player, int id) {
      List<String> list = (List<String>) this.reminder.getList(player);
      if (list == null) {
         list = new ArrayList();
      }

      ((List)list).add(String.valueOf(id));
      this.reminder.set(player, list);
   }

   public boolean removeReminder(String player, int id) {
      String sid = String.valueOf(id);
      List<String> list = (List<String>) this.reminder.getList(player);
      if (list != null) {
         if (list.remove(sid)) {
            if (list.size() == 0) {
               this.reminder.set(player, (Object)null);
            } else {
               this.reminder.set(player, list);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public List<String> getReminder(String player) {
      return (List<String>) this.reminder.getList(player);
   }

   public boolean hasReminder(String player) {
      return this.reminder.contains(player);
   }
}
