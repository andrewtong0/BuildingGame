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
        int musicId = Integer.parseInt(args[0]);
        playMusic(vars.getParticipants(), musicId);
        return true;
    }

    public void playMusic(List<Player> participants, int musicId) {
        for (Player p : participants) {
            // First float represents volume, second is speed
            if (musicId == 0) {
                p.playSound(p.getLocation(), "music_disc.far", 0.25f, 1.0f);
            } else if (musicId == 1) {
                p.playSound(p.getLocation(), "music_disc.ward", 0.25f, 1.0f);
            } else if (musicId == 2) {
                p.playSound(p.getLocation(), "music_disc.11", 0.25f, 1.0f);
            } else if (musicId == 3) {
                p.playSound(p.getLocation(), "music_disc.13", 0.25f, 1.0f);
            }
        }
    }
}
