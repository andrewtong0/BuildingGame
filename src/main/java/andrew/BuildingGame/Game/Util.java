package andrew.BuildingGame.Game;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
  public static void sendTitleMessage(Player player, String message) {
    IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + message + "\"}"));
    PacketPlayOutTitle p = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
  }

  public static void sendActionBarMessage(Player player, String message) {
    TextComponent prompt = new TextComponent(message);
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, prompt);
  }

  public static BossBar createBossBarTimer(int initialSeconds) {
    int minutes = initialSeconds / 60;
    int seconds = initialSeconds % 60;
    BossBar bar = Bukkit.createBossBar(
            ChatColor.WHITE + "Time Remaining: " + timePadZeroes(minutes, false) + ":" + timePadZeroes(seconds, true),
            BarColor.GREEN,
            BarStyle.SOLID
    );
    bar.setVisible(true);
    return bar;
  }

  public static void updateBossBarTimer(BossBar bar, int currentSeconds, int totalSeconds) {
    int minutes = currentSeconds / 60;
    int seconds = currentSeconds % 60;
    double currentProgress = (double) currentSeconds / totalSeconds;
    String newTitle = ChatColor.WHITE + "Time Remaining: " + timePadZeroes(minutes, false) + ":" + timePadZeroes(seconds, true);
    bar.setColor(determineBarColour(currentProgress));
    bar.setProgress(currentProgress);
    bar.setTitle(newTitle);
  }

  private static String timePadZeroes(int time, boolean isSeconds) {
    if (time == 0) { return "00"; }
    else if (isSeconds && 10 > time && time > 0) { return "0" + time; }
    else { return String.valueOf(time); }
  }

  public static BarColor determineBarColour(double percentage) {
    if (percentage > 0.66) { return BarColor.GREEN; }
    else if (percentage > 0.33) { return BarColor.YELLOW; }
    else { return BarColor.RED; }
  }

  public static void addPlayersToBossBar(List<Player> players, BossBar bar) {
    for (Player p : players) bar.addPlayer(p);
  }

  public static Location offsetTeleport(GameSettings settings, Location l) {
    int xTeleportOffset = settings.getBuildAreaWidth() / 2 + settings.getBuildAreaPadding();
    int halfPadding = settings.getBuildAreaPadding() / 2;
    int zTeleportOffset = halfPadding > 0 ? halfPadding : 1;
    return new Location(
            l.getWorld(),
            l.getX() + xTeleportOffset,
            l.getY(),
            l.getZ() + zTeleportOffset
    );
  }

  public static HashMap<Player, Player> reversePlayerChain(HashMap<Player, Player> playerChain) {
    HashMap<Player, Player> reverseChain = new HashMap<>();
    for (Map.Entry<Player, Player> p : playerChain.entrySet()) {
      reverseChain.put(p.getValue(), p.getKey());
    }
    return reverseChain;
  }

  public static void sendMessageToPlayers(List<Player> participants, String message) {
    for (Player p : participants) { p.sendMessage(message); }
  }

  public static void sendCustomJsonMessage(Player p, String jsonString) {
    p.getServer().dispatchCommand(p.getServer().getConsoleSender(), "tellraw " + p.getName() + " " + jsonString);
  }

  public static void sendCustomJsonMessage(List<Player> participants, String jsonString) {
    for (Player p : participants) { sendCustomJsonMessage(p, jsonString); }
  }
}
