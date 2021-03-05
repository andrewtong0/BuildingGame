package andrew.BuildingGame.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BGPrompt implements CommandExecutor {
  private static HashMap<Player, List<String>> prompts = new HashMap<>();
  boolean onlyOnePromptAllowed = true;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player player = (Player) sender;
    String promptString = String.join(" ", args);
    if (prompts.containsKey(player)) {
      if (onlyOnePromptAllowed) {
        sender.sendMessage("Replaced prompt with: " + promptString);
        prompts.get(player).remove(0);
        prompts.get(player).add(promptString);
      } else {
        sender.sendMessage("Added prompt: " + promptString);
        prompts.get(player).add(promptString);
      }
    } else {
      sender.sendMessage("Added prompt: " + promptString);
      ArrayList<String> playerPrompts = new ArrayList<>();
      playerPrompts.add(promptString);
      prompts.put(player, playerPrompts);
    }

    if (BGReady.getParticipants().size() == prompts.size()) {
      if (BGHost.getHost() != null) { BGHost.getHost().sendMessage("All players have entered prompts!"); }
      else { Bukkit.broadcastMessage(ChatColor.GREEN + "All players have entered prompts!"); }
    }

    return true;
  }

  public static HashMap<Player, List<String>> getPrompts() { return prompts; }
}
