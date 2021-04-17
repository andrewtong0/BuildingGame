package andrew.BuildingGame.Game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class GameVars {
  Player host;
  World world;
  Location origin;
  List<Player> participants;
  int numPlayers;
  int numRounds;
  int numBuildRounds;

  public GameVars(Player host, World world, List<Player> participants) {
    this.host = host;
    this.world = world;
    this.participants = participants;

    origin = host.getLocation();
    numPlayers = participants.size();
    numRounds = calculateNumRounds();
  }

  public Player getHost() {
    return host;
  }
  public World getWorld() {
    return world;
  }
  public Location getOrigin() {
    return origin;
  }
  public List<Player> getParticipants() {
    return participants;
  }
  public int getNumPlayers() {
    return numPlayers;
  }
  public int getNumRounds() {
    return numRounds;
  }
  public int getNumBuildRounds() {
    if (numPlayers > 4) {
      return 3;
    } else {
      return 2;
    }
  }
  private int calculateNumRounds() {
    if (numPlayers > 4) {
      return 7;
    } else {
      return 4;
    }
  }
}
