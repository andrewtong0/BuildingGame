package andrew.BuildingGame.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class BGPrompt implements CommandExecutor {
  private static HashMap<Player, String> prompts = new HashMap<>();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player player = (Player) sender;
    String promptString = String.join(" ", args);
    if (prompts.containsKey(player)) { sender.sendMessage("Replaced prompt with: " + promptString); }
    else { sender.sendMessage("Added prompt: " + promptString); }
    prompts.put(player, promptString);

    if (BGReady.getParticipants().size() == prompts.size()) {
      if (BGHost.getHost() != null) { BGHost.getHost().sendMessage("All players have entered prompts!"); }
      else { Bukkit.broadcastMessage(ChatColor.GREEN + "All players have entered prompts!"); }
    }

    return true;
  }

  public static HashMap<Player, String> getPrompts() { return prompts; }

  public static void clearPrompts() { prompts = new HashMap<>(); }
}
