package de.xcraft.voronwe.XcraftTickets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public enum Msg {
   NONE("none"),
   BREAK("&8-----------------------------------------------------"),
   ERR_NOT_FROM_CONSOLE("&cYou can't use this command from the console."),
   ERR_TICKET_NOT_FOUND("Could not find a ticket with the id $ID$!"),
   ERR_TICKET_NO_PERMISSION("You dont have permission to access that ticket!"),
   ERR_MOD_NOT_FOUND("Could not find an assignee with that name: $NAME$."),
   ERR_ASSIGNEE_NOT_FOUND("A assignee with the name $name$ does not exist!"),
   ERR_PHRASE_NOT_FOUND("Could not find an phrase with that name: $NAME$."),
   ERR_UNDO_IMPOSSIBLE("This undo is impossible, sorry."),
   COMMAND_MOD("Edit all assignees."),
   COMMAND_MOD_LIST("current assignees: $MISC$."),
   COMMAND_MOD_ADD("Successfully added the assignee $NAME$."),
   COMMAND_MOD_REMOVE("Successfully removed the assignee $NAME$."),
   COMMAND_STATS("Show current Ticket-Stats."),
   COMMAND_ASSIGN("Assign a ticket."),
   COMMAND_PHRASES("Edit all phrases."),
   COMMAND_PHRASES_LIST("current phrases:"),
   COMMAND_PHRASES_ADD("Successfully added the phrase $NAME$: $MESSAGE$"),
   COMMAND_PHRASES_REMOVE("Successfully removed the phrase $NAME$"),
   COMMAND_PHRASES_APPEND("Successfully extended the phrase $NAME$: $MESSAGE$"),
   COMMAND_PHRASES_ENTRY("&e$NAME$: &f$MESSAGE$"),
   COMMAND_UNASSIGN("Save all data to disc."),
   COMMAND_UNDO("Revokes the last action."),
   COMMAND_UNDO_SUCCESSFUL("Undo successful"),
   COMMAND_WARP("Warp to a ticket."),
   COMMAND_CLOSE("Close a ticket."),
   COMMAND_LIST("List all tickets."),
   COMMAND_LOG("Comment to a ticket."),
   COMMAND_OPEN("Open a ticket."),
   COMMAND_OPEN_SUCCESSFUL("Thank you, $NAME$! Your ticket-id is #$ID$! A Admin/Mod will deal with you ticket soon!"),
   COMMAND_REOPEN("Re-open a closed ticket."),
   COMMAND_SETWARP("Change the warp of a ticket."),
   COMMAND_VIEW("Show all information of a ticket."),
   USAGE_ID("#"),
   USAGE_NO_TICKET_ID("&cYou need to provide a ticket-id."),
   USAGE_MESSAGE("Message"),
   USAGE_NO_MESSAGE("&cYou need to provide a message."),
   USAGE_ASSIGNEE("Name/Group"),
   USAGE_NO_ASSIGNEE("&cYou need to provide a assignee"),
   TICKET_BROADCAST_ASSIGN("Ticket #$ID$ was assigned to $NAME$"),
   TICKET_BROADCAST_UNASSIGN("The assignment of ticket #$ID$ has been removed!"),
   TICKET_BROADCAST_WARP("$NAME$ is processing ticket #$ID$"),
   TICKET_BROADCAST_CLOSE("Ticket #$ID$ was closed by $NAME$: $MESSAGE$"),
   TICKET_BROADCAST_COMMENT("Ticket #$ID$ was commented by $NAME$: $MESSAGE$"),
   TICKET_BROADCAST_OPEN("Ticket #$ID$ was opened by $NAME$: $MESSAGE$"),
   TICKET_BROADCAST_REOPEN("Ticket #$ID$ was reopened by $NAME$: $MESSAGE$"),
   TICKET_BROADCAST_SETWARP("The warp of Ticket #$ID$ was updated by $NAME$"),
   TICKET_VIEW_INFO("&3Info for ticket &6#$ID$ &3from &e$NAME$ &3$ASSIGNEE$"),
   TICKET_VIEW_ASSIGNEE("assigned to &5$NAME$"),
   TICKET_VIEW_BREAK("--------------------------------------------------"),
   TICKET_VIEW_OPEN("&8$TIME$&f | &aopened &fby &e$NAME$&f: $MESSAGE$"),
   TICKET_VIEW_COMMENT("&8$TIME$&f | &e$NAME$&f: $MESSAGE$"),
   TICKET_VIEW_CLOSE("&8$TIME$&f | &cclosed &fby &e$NAME$&f: $MESSAGE$"),
   TICKET_VIEW_SETWARP("&8$TIME$&f | &bwarp updated &fby &e$NAME$&f: $MESSAGE$"),
   TICKET_VIEW_REOPEN("&8$TIME$&f | &9reopened &fby &e$NAME$&f: $MESSAGE$"),
   TICKET_VIEW_ASSIGN("&8$TIME$&f | &5assigend &fby &e$NAME$&f: to &5$ASSIGNEE$"),
   TICKET_LIST_HEAD("Ticketlist"),
   TICKET_LIST_EMTPY("There are no open tickets..."),
   TICKET_LIST_MISC_ONLINE("&f[&2+&f]"),
   TICKET_LIST_MISC_OFFLINE("&f[&4-&f]"),
   TICKET_LIST_COMMENT_READ("&8[&7$COMMENTS$ comments&8]"),
   TICKET_LIST_COMMENT_UNREAD("&8[&b$COMMENTS$ comments&8]"),
   TICKET_LIST_ASSIGNEE("assigned to &5$NAME$"),
   TICKET_LIST("&6#$ID$ &e$NAME$ &f&o$MESSAGE$"),
   TICKET_LIST_HOVER("&6#$ID$ &8$TIME$ $MISC$ &e$NAME$ &7$ASSIGNEE$&f\n&f&o$MESSAGE$"),
   TICKET_REMIND_CLOSE("Your ticket $ID$ was closed. Please revisit! (/ticket view $ID$)"),
   TICKET_REMIND_CLOSE_LIST("You have $ID$ unread closed Tickets! (/ticket view <ID>) ($MISC$)"),
   TICKET_REMIND_UNREAD("You have unread messages in you Ticket $ID$! (/ticket view $ID$)"),
   TICKET_REMIND_UNREAD_LIST("You have $MISC$ unread Tickets! (/ticket list)");

   private String msg;

   private Msg(String msg) {
      this.set(msg);
   }

   private void set(String output) {
      this.msg = output;
   }

   private String get() {
      return this.msg;
   }

   public String toString() {
      String message = this.msg.replaceAll("&([0-9a-z])", "§$1");
      message = message.replace("\\n", "\n");
      return message;
   }

   public String toString(Msg.Replace r1) {
      String message = this.toString();
      message = message.replace(r1.name(), r1.get());
      return message;
   }

   public String toString(Msg.Replace r1, Msg.Replace r2) {
      String message = this.toString();
      message = message.replace(r1.name(), r1.get());
      message = message.replace(r2.name(), r2.get());
      return message;
   }

   public String toString(Msg.Replace... repl) {
      String message = this.toString();
      Msg.Replace[] var3 = repl;
      int var4 = repl.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Msg.Replace r = var3[var5];
         message = message.replace(r.name(), r.get());
      }

      return message;
   }

   public static void init(XcraftPlugin plugin) {
      File msgFile = new File(plugin.getDataFolder(), "locale.yml");
      if (load(plugin, msgFile)) {
         parseFile(plugin, msgFile);
      }
   }

   private static boolean load(XcraftPlugin plugin, File file) {
      if (file.exists()) {
         return true;
      } else {
         try {
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            Msg[] var3 = values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Msg m = var3[var5];
               String msg = m.get();
               if (msg.contains("\n")) {
                  msg = msg.replace("\n", "\\n");
               }

               bw.write(m.name() + ": " + msg);
               bw.newLine();
            }

            bw.close();
            return true;
         } catch (Exception var8) {
            plugin.getMessenger().warning("Couldn't initialize locale.yml. Using defaults.");
            return false;
         }
      }
   }

   private static void parseFile(XcraftPlugin plugin, File file) {
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
            process(plugin, s);
         }

         br.close();
      } catch (Exception var7) {
         var7.printStackTrace();
         plugin.getMessenger().warning("Problem with locale.yml. Using defaults.");
      }
   }

   private static void process(XcraftPlugin plugin, String s) {
      String[] split = s.split(": ", 2);

      try {
         Msg msg = valueOf(split[0]);
         msg.set(split[1]);
      } catch (Exception var4) {
         plugin.getMessenger().warning(split[0] + " is not a valid key. Check locale.yml.");
      }
   }

   public static enum Replace {
      $NAME$("Name of a Player"),
      $ID$("ID of a ticket"),
      $MESSAGE$("Message provided in a command"),
      $TIME$("timesamp for a ticket"),
      $ASSIGNEE$("Assignee of a ticket"),
      $COMMENTS$("comments.."),
      $MISC$("Miscellaneous stuff");

      private String key;

      private Replace(String key) {
         this.set(key);
      }

      private void set(String output) {
         this.key = output;
      }

      private String get() {
         return this.key;
      }

      public static Msg.Replace NAME(String replace) {
         $NAME$.set(replace);
         return $NAME$;
      }

      public static Msg.Replace ID(int replace) {
         $ID$.set(String.valueOf(replace));
         return $ID$;
      }

      public static Msg.Replace MESSAGE(String replace) {
         $MESSAGE$.set(replace);
         return $MESSAGE$;
      }

      public static Msg.Replace TIME(String replace) {
         $TIME$.set(replace);
         return $TIME$;
      }

      public static Msg.Replace ASSIGNEE(String replace) {
         $ASSIGNEE$.set(replace);
         return $ASSIGNEE$;
      }

      public static Msg.Replace COMMENTS(String replace) {
         $COMMENTS$.set(replace);
         return $COMMENTS$;
      }

      public static Msg.Replace MISC(String replace) {
         $MISC$.set(replace);
         return $MISC$;
      }
   }
}
