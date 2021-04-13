package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BGNext implements CommandExecutor {
  private static Game game;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    game = Main.getGame();
    game.advancePhase();
    return true;
  }
}
