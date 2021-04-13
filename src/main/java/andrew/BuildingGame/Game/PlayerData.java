package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class PlayerData {
  Player player;
  List<Location> path;
  Location currLocation;

  public PlayerData(Player player, List<Location> path) {
    this.player = player;
    this.path = path;
  }

  public void setLocation(Location currLocation) {
    this.currLocation = currLocation;
  }

  public Player getPlayer() { return player; }

  public Location getLocationAtIndex(int index) { return path.get(index); }

  public Location getLocation() {
    return currLocation;
  }
}
