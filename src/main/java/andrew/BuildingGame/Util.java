package andrew.BuildingGame;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
}
