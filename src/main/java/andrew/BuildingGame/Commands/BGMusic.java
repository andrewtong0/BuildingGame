package andrew.BuildingGame.Commands;

import andrew.BuildingGame.Game.Game;
import andrew.BuildingGame.Game.GameVars;
import andrew.BuildingGame.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BGMusic implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Game game = Main.getGame();
        GameVars vars = game.getVars();
        playMusic(vars.getParticipants());
        return true;
    }

    public void playMusic(List<Player> participants) {
        for (Player p : participants) {
            // First float represents volume, second is speed
            p.playSound(p.getLocation(), "music_disc.ward", 0.25f, 1.0f);
        }
    }
}
