package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameSettings;
import andrew.BuildingGame.Game.GameVars;
import andrew.BuildingGame.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BGStart implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Main.createGame();
    Game game = Main.getGame();
    game.startGame();
    return true;
  }
}

