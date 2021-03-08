package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BGTour implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Main.getGame().tourBuilds();
    return true;
  }
}
