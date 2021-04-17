package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BGTimer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Game game = Main.getGame();
        if (args.length > 0) {
            int timerDuration = Integer.parseInt(args[0]);
            game.startTimer(timerDuration);
        } else {
            game.startTimer(game.getSettings().getBuildTimeSeconds());
        }
        return true;
    }
}
