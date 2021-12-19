package de.xcraft.voronwe.XcraftTickets;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log {
   private final DateFormat formatter;
   private final List<Log.LogEntry> entries;

   public Log(DateFormat formatter) {
      this.formatter = formatter;
      this.entries = new ArrayList();
   }

   public void add(Log.EntryType type, String player, String message) {
      long time = (new Date()).getTime();
      this.entries.add(new Log.LogEntry(time, type, player, message));
   }

   public void add(long time, Log.EntryType type, String player, String message) {
      this.entries.add(new Log.LogEntry(time, type, player, message));
   }

   public Log.LogEntry getEntry(int index) {
      return (Log.LogEntry)this.entries.get(index);
   }

   public boolean remove(Log.LogEntry entry) {
      return this.entries.remove(entry);
   }

   public int size() {
      return this.entries.size();
   }

   public String getDate() {
      return this.formatter.format(new Date(((Log.LogEntry)this.entries.get(0)).time));
   }

   public String getEntryOutput(int index, int ticketId) {
      Log.LogEntry entry = (Log.LogEntry)this.entries.get(index);
      Msg.Replace[] replace = new Msg.Replace[]{Msg.Replace.NAME(entry.player), Msg.Replace.TIME(this.formatter.format(new Date(entry.time))), Msg.Replace.MESSAGE(entry.message), Msg.Replace.ID(ticketId), Msg.Replace.MISC(String.valueOf(index))};
      switch(entry.type) {
      case OPEN:
         return Msg.TICKET_VIEW_OPEN.toString(replace);
      case COMMENT:
         return Msg.TICKET_VIEW_COMMENT.toString(replace);
      case CLOSE:
         return Msg.TICKET_VIEW_CLOSE.toString(replace);
      case REOPEN:
         return Msg.TICKET_VIEW_REOPEN.toString(replace);
      case ASSIGN:
         replace[2] = Msg.Replace.ASSIGNEE(entry.message);
         return Msg.TICKET_VIEW_ASSIGN.toString(replace);
      case SETWARP:
         return Msg.TICKET_VIEW_SETWARP.toString(replace);
      default:
         return null;
      }
   }

   public String[] getEntries(int ticketId) {
      String[] list = new String[this.entries.size()];

      for(int i = 0; i < this.entries.size(); ++i) {
         list[i] = this.getEntryOutput(i, ticketId);
      }

      return list;
   }

   public class LogEntry {
      public final long time;
      public final String player;
      public final String message;
      public final Log.EntryType type;

      public LogEntry(long time, Log.EntryType type, String player, String message) {
         this.time = time;
         this.player = player;
         this.message = message;
         this.type = type;
      }

      public String toString() {
         return this.time + "; " + this.player + "; " + this.type.name() + "; " + this.message;
      }
   }

   public static enum EntryType {
      OPEN,
      COMMENT,
      CLOSE,
      REOPEN,
      ASSIGN,
      SETWARP;
   }
}
