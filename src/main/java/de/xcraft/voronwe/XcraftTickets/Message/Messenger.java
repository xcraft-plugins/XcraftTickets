package de.xcraft.voronwe.XcraftTickets.Message;

import de.xcraft.voronwe.XcraftTickets.XcraftPlugin;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messenger {
   private static final Logger log = Logger.getLogger("Minecraft");
   private String prefix = "";

   public Messenger(XcraftPlugin plugin) {
      this.prefix = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + plugin.getDescription().getName() + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
   }

   public boolean sendInfo(CommandSender sender, String msg, boolean showPrefix) {
      if (sender != null && msg != null && !msg.equals(" ")) {
         msg = ChatColor.translateAlternateColorCodes('&', msg);
         if (showPrefix) {
            msg = this.prefix + msg;
         }

         TextComponent message = new TextComponent();
         String[] var5 = msg.split("[$]TEXT[$]");
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String component = var5[var7];
            String[] hover = component.split("[$]HOVER[$]", 2);
            TextComponent newComponent;
            if (hover[0].startsWith("$LIMIT ")) {
               String[] limit = hover[0].substring(7).split("[$]", 2);
               if (limit.length != 2) {
                  newComponent = new TextComponent(hover[0]);
               } else {
                  int size;
                  try {
                     size = Integer.parseInt(limit[0]);
                  } catch (NumberFormatException var14) {
                     size = Integer.MAX_VALUE;
                  }

                  newComponent = new TextComponent(limit(limit[1], size));
               }
            } else {
               newComponent = new TextComponent(hover[0]);
            }

            if (hover.length == 2 && !hover[1].isEmpty() && detectExtra(newComponent, hover[1], "[$]COMMAND[$]", Action.RUN_COMMAND) && detectExtra(newComponent, hover[1], "[$]SUGGEST[$]", Action.SUGGEST_COMMAND) && detectExtra(newComponent, hover[1], "[$]URL[$]", Action.OPEN_URL)) {
               newComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(hover[1])}));
            }

            message.addExtra(newComponent);
         }

         sender.spigot().sendMessage(message);
         return true;
      } else {
         return false;
      }
   }

   private static boolean detectExtra(BaseComponent newComponent, String s, String s2, Action suggestCommand) {
      String[] part = s.split(s2, 2);
      if (part.length == 2 && !part[1].isEmpty()) {
         newComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Content[]{new Text(part[0])}));
         newComponent.setClickEvent(new ClickEvent(suggestCommand, part[1]));
         return false;
      } else {
         return true;
      }
   }

   private static String limit(String msg, int size) {
      if (msg.isEmpty()) {
         return "";
      } else {
         int charWidth = getSize(msg.charAt(0));
         if (msg.startsWith("§")) {
            return "" + msg.charAt(0) + msg.charAt(1) + limit(msg.substring(2), size);
         } else {
            return charWidth <= size - 4 ? msg.charAt(0) + limit(msg.substring(1), size - charWidth) : "...";
         }
      }
   }

   private static int getSize(char c) {
      switch(c) {
      case ' ':
      case 'I':
      case '[':
      case ']':
      case 't':
         return 4;
      case '!':
      case ',':
      case '.':
      case ':':
      case ';':
      case 'i':
      case '|':
         return 2;
      case '"':
      case '(':
      case ')':
      case '*':
      case '<':
      case '>':
      case 'f':
      case 'k':
      case '{':
      case '}':
         return 5;
      case '#':
      case '$':
      case '%':
      case '&':
      case '+':
      case '-':
      case '/':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case '=':
      case '?':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '\\':
      case '^':
      case '_':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'g':
      case 'h':
      case 'j':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
      case 'Ä':
      case 'Ü':
      case 'ä':
      case 'ö':
      case 'ü':
         return 6;
      case '\'':
      case '`':
      case 'l':
         return 3;
      case '@':
      case '~':
      case '\u007f':
      case '\u0080':
      case '\u0081':
      case '\u0082':
      case '\u0083':
      case '\u0084':
      case '\u0085':
      case '\u0086':
      case '\u0087':
      case '\u0088':
      case '\u0089':
      case '\u008a':
      case '\u008b':
      case '\u008c':
      case '\u008d':
      case '\u008e':
      case '\u008f':
      case '\u0090':
      case '\u0091':
      case '\u0092':
      case '\u0093':
      case '\u0094':
      case '\u0095':
      case '\u0096':
      case '\u0097':
      case '\u0098':
      case '\u0099':
      case '\u009a':
      case '\u009b':
      case '\u009c':
      case '\u009d':
      case '\u009e':
      case '\u009f':
      case ' ':
      case '¡':
      case '¢':
      case '£':
      case '¤':
      case '¥':
      case '¦':
      case '§':
      case '¨':
      case '©':
      case 'ª':
      case '«':
      case '¬':
      case '\u00ad':
      case '®':
      case '¯':
      case '°':
      case '±':
      case '²':
      case '³':
      case '´':
      case 'µ':
      case '¶':
      case '·':
      case '¸':
      case '¹':
      case 'º':
      case '»':
      case '¼':
      case '½':
      case '¾':
      case '¿':
      case 'À':
      case 'Á':
      case 'Â':
      case 'Ã':
      case 'Å':
      case 'Æ':
      case 'Ç':
      case 'È':
      case 'É':
      case 'Ê':
      case 'Ë':
      case 'Ì':
      case 'Í':
      case 'Î':
      case 'Ï':
      case 'Ð':
      case 'Ñ':
      case 'Ò':
      case 'Ó':
      case 'Ô':
      case 'Õ':
      case 'Ö':
      case '×':
      case 'Ø':
      case 'Ù':
      case 'Ú':
      case 'Û':
      case 'Ý':
      case 'Þ':
      case 'ß':
      case 'à':
      case 'á':
      case 'â':
      case 'ã':
      case 'å':
      case 'æ':
      case 'ç':
      case 'è':
      case 'é':
      case 'ê':
      case 'ë':
      case 'ì':
      case 'í':
      case 'î':
      case 'ï':
      case 'ð':
      case 'ñ':
      case 'ò':
      case 'ó':
      case 'ô':
      case 'õ':
      case '÷':
      case 'ø':
      case 'ù':
      case 'ú':
      case 'û':
      default:
         return 7;
      }
   }

   public void info(String msg) {
      log.info(msg);
   }

   public void warning(String msg) {
      log.warning(msg);
   }

   public void severe(String msg) {
      log.severe(msg);
   }
}
